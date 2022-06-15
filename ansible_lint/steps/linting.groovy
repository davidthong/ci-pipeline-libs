package libraries.ansible_lint.steps

void call(){

    // TODO: bump all of these standard directory structures up to an init stage of the pipeline
    // could we use a standard directory layout???
    // e.g. - https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html

    build_test_reports_dir = 'build/test-reports'
    src_test_resources_dir = 'src/test/resources'

    sh "mkdir -p ${build_test_reports_dir}"
    sh "mkdir -p ${src_test_resources_dir}"

    // TODO: impl check if resource does not exist, fixup config inclusion so that it doesnt reference something that does not exist
    String yamllint_config = resource("yamllint.config")

    writeFile(file: "${src_test_resources_dir}/yamllint.config", text: yamllint_config)

    stage('YAML Lint'){
        // run yamllint on workspace
        docker.image('pipelinecomponents/yamllint:latest').withRun("-t -v ${WORKSPACE}:/workspace -w /workspace --entrypoint sh"){ c ->
            sh "docker exec ${c.id} pip install yamllint-junit"
            sh "docker exec ${c.id} sh -c 'yamllint -c ${src_test_resources_dir}/yamllint.config -f parsable . | yamllint-junit -o ${build_test_reports_dir}/yaml-lint.xml'"
        }

        junit allowEmptyResults: true, skipPublishingChecks: true, testResults: "${build_test_reports_dir}/yaml-lint.xml"
    }
   
    stage("Ansible Lint"){
        // run ansible-lint on workspace
        docker.image('pipelinecomponents/ansible-lint:latest').withRun("-t -v ${WORKSPACE}:/workspace -w /workspace --entrypoint sh"){ c ->

            // TODO: check if there is a requirements file to use first, else no-op
            sh "docker exec ${c.id} ansible-lint --version "

            requirementsFileExists = fileExists './collections/requirements.yml'

            if(requirementsFileExists){
                sh "docker exec ${c.id} ansible-galaxy collection install -r ./collections/requirements.yml"                
            }else{
                println("./collections/requirements.yml does not exist - Skipping")
            }
            
            sh "docker exec ${c.id} sh -c 'ansible-lint -v --show-relpath --parseable-severity --nocolor . | ansible-lint-junit -o ${build_test_reports_dir}/ansible-lint.xml'"
        }
   
        junit allowEmptyResults: true, skipMarkingBuildUnstable: true, skipPublishingChecks: true, testResults: "${build_test_reports_dir}/ansible-lint.xml"

    }   
}
