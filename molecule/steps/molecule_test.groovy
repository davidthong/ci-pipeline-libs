package libraries.molecule.steps

void call(){

  ansiColor('xterm') {

    withPythonEnv('python3') {

      stage ("Set Up Virtual Environment") {

        envRequirements = './tests/resources/requirements.yml'
        
        // update tools and requirements
        sh 'pip3 install --upgrade pip setuptools'

        requirementsFileExists = fileExists "${envRequirements}"

        if(requirementsFileExists){
          sh "pip3 install --upgrade -r ${envRequirements}"
        }
      }
            
      stage ("Executing Molecule Test") {
        sh 'hostname'
        sh 'dir .'
        sh 'molecule test'
      }          
    }
  }
} 