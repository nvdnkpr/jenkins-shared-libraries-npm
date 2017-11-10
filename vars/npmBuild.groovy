#!/usr/bin/groovy
def call() {
  pipeline {
    agent any

    stages {
      stage('Bootstrap'){
        steps {
          // Read Configuration
          // Read main config
          mainConfig = configFileReader('main', '', true)
          // Read stage configurations
          mainConfig.configurations.each { fileId -> configFileReader(fileId, "${fileId.replace('-','_')}", true) }

          setGitConfig(env.GIT_NAME, env.GIT_MAIL)
          getRepoInfo()
          getBuildContext()

          // checkout code from SCM
          checkout([
            $class: 'GitSCM',
            branches: [[name: "*/${env.REAL_BRANCH_NAME}"]],
            extensions: [
              [$class: 'CleanBeforeCheckout'],
              [$class: 'LocalBranch', localBranch: "${env.REAL_BRANCH_NAME}"]
            ]
          ])

          // Setup Tools
          setupTools()          
        }
      }
      stage('Prepare'){
        steps {
          // package
          checkPackageJson()

          // update packages
          updateNPMPackages()

          runLinter()
        }
        // post {
        //   success {
        //     // publish linting report
        //   }
        // }
      }
      stage('Build'){
        steps {
          if (env.HAS_BUILD) { compileMain() }
          if (env.HAS_BUILD_E2E) { compileAndDeployE2E() }
        }
      }

      stage('Test'){
        steps {
          if (env.HAS_TEST) { runUnitTests() }
          if (env.HAS_TEST_E2E) { runE2ETests() }

          send2SonarQube()
        }
        // post {
        //   success {
        //     // publish coverage report
        //   }
        // }
      }
      stage('Publish'){
        steps {
          publish2NpmRepo()
        }
        // Publish to Nexus
      }
    }
    // post {
    //   always {
    //     // notify via email, skype, slack... you name it
    //   }
    //   changed { }
    //   failure { }
    //   success { }
    //   unstable { }
    //   aborted { }
    // }
  }
}