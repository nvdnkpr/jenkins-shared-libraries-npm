#!/usr/bin/groovy
def call() {
  pipeline {
    agent any

    stages {
      stage('Bootstrap'){
        steps {
          // Read Configuration
          readConfiguration()
          
          // set and get info about this build
          getRepoInfo()
          getBuildContext()

          // checkout code from SCM
          checkout scm

          // Setup Tools
          setupTools()
        }
      }
      stage('Prepare'){
        steps {
          // package
          checkPackageJson()
          echo sh(script: 'env|sort', returnStdout: true)
          
          // update packages
          updateNPMPackages()
          // error 'updatePackages'

          runLinter()
          // error 'linter'
        }
        // post {
        //   success {
        //     // publish linting report
        //   }
        // }
      }
      stage('Build'){
        steps {
          compileMain()
          compileAndDeployE2E()
        }
      }

      stage('Test'){
        steps {
          runUnitTests()
          
          runE2ETests()

          send2SonarQube()
          
        }
        post {
          always {
            publishJUnitReport()
          }
          success {
            publishNYCReport()
          }
        }
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