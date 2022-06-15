# ci-pipeline-libs
The following libraries provide support for Continuous Integration testing for Ansible automation content.

# YAML Lint and Ansible Lint
Provides implementation for YAML and Ansible Linting.  Collects and reports findings in Jenkins' Test Reporting.  YAML linting is configured as follows:

```yaml
---
# Based on ansible-lint config
extends: default

ignore: |
  molecule_venv/
  .pyenv*/
  
rules:
  braces:
    max-spaces-inside: 1
    level: error
  brackets:
    max-spaces-inside: 1
    level: error
  colons:
    max-spaces-after: -1
    level: error
  commas:
    max-spaces-after: -1
    level: error
  comments: disable
  comments-indentation: disable
  document-start: disable
  empty-lines:
    max: 3
    level: error
  hyphens:
    level: error
  indentation: disable
  key-duplicates: enable
  line-length: disable
  new-line-at-end-of-file: disable
  new-lines:
    type: unix
  trailing-spaces: disable
  truthy: disable
```

# Git
Provides Git SCM hooks to detect repository events (commits, pull requests, merges).  

# SDP
Provides helper library resources used by Git library.

# Molecule
Provides implementation for running Molecule scenarios in a CI pipeline.