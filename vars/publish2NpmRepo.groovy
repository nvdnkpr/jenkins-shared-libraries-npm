#!/usr/bin/groovy

def call(){
  withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: env.MAIN_CREDENTIALS_GIT_PROVIDER, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USER']]) {
    def gitUser = URLEncoder.encode(env.GIT_USER, 'UTF-8')
    credentialsUrl = env.REMOTE_URL.replace('://',"://${gitUser}:${env.GIT_PASSWORD}@")
    
    withCredentials([string(credentialsId: env.MAIN_CREDENTIALS_ARTIFACT_REPO, variable: 'NPM_TOKEN')]) {
      withNPM(npmrcConfig: env.PUBLISHING_NPMRC_CONFIG) {
        // set necessary credentials in global config
        sh 'echo "/n" >> .npmrc'
        sh "echo _auth=$NPM_TOKEN >> .npmrc"
        
        // different publishin ways for different branches
        def branchType = env.REAL_BRANCH_NAME.split("/")[0]
        def version = env.PACKAGE_JSON_VERSION
        def npmChannel = "latest"

        def isPublishingBranch = ['develop', 'release', 'master'].contains(branchType)
        if (isPublishingBranch){
          switch(branchType) {
            case 'develop':
              version = "${env.PACKAGE_JSON_VERSION}-${env.PUBLISHING_DEVELOP_BUILD_TAG}.${env.BUILD_NUMBER}"
              npmChannel = env.PUBLISHING_DEVELOP_NPM_CHANNEL
              break

            case 'release':
              version = "${env.PACKAGE_JSON_VERSION}-${env.PUBLISHING_RELEASE_BUILD_TAG}.${env.BUILD_NUMBER}"
              npmChannel = env.PUBLISHING_RELEASE_NPM_CHANNEL
              break
            
            case 'master':
              // the variables were already defined in the package.json
              break
            default:
              echo "No Publishing Branch"
              break
          }

          // let version & publish
          sh "npm version $version --no-git-tag-version  --allow-same-version"
          sh "npm publish --tag $npmChannel"
          // tag pushing only on mastergst
          
          /*
          if (branchType == "master"){
            sh "git tag -a \"v$version\" -m \"$version\""
            sh "git push --tags"
          }
          */

        } else {
          echo "No publishing branch"
        }
        
        sh "rm .npmrc"
      }
    }
  }
}
