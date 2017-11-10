#!/usr/bin/groovy

def call(){
  withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: env.MAIN_CREDENTIALS_GIT_PROVIDER, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USER']]) {
    def gitUser = URLEncoder.encode(env.GIT_USER, 'UTF-8')
    credentialsUrl = env.REMOTE_URL.replace('://',"://${gitUser}:${env.GIT_PASSWORD}@")
    
    withCredentials([string(credentialsId: env.MAIN_CREDENTIALS_ARTIFACT_REPO, variable: 'NPM_TOKEN')]) {
      withNPM(npmrcConfig: env.PUBLISHING_NPMRC_CONFIG) {
        // set necessary credentials in global config
        sh 'echo "/n" >> .npmrc'
        sh "echo _auth=${env.NPM_TOKEN} >> .npmrc"
        
        // different publishin ways for different branches
        def version, npmChannel
        def branchType = env.REAL_BRANCH_NAME.split("/")[0]
        if (['develop', 'release', 'master'].containsValue(branchType)){
          switch(branchType) {
            case 'develop':
              version = "${env.PROJECT_VERSION}-${env.PUBLISHING_DEVELOP_BUILD_TAG}.${env.BUILD_NUMBER}"
              npmChannel = env.PUBLISHING_DEVELOP_NPM_CHANNEL
              break
            case 'release':
              version = "${env.PROJECT_VERSION}-${env.PUBLISHING_RELEASE_BUILD_TAG}.${env.BUILD_NUMBER}"
              npmChannel = env.PUBLISHING_RELEASE_NPM_CHANNEL
              break
            
            case 'master':
              version = env.PROJECT_VERSION
              npmChannel = 'latest'
              break
            default:
              echo "No Publishing Branch"
              break
          }

          // let version & publish
          sh sprintf("npm version %s --no-git-tag-version", version)
          sh sprintf("npm publish --tag %s", npmChannel)
          sh sprintf("git tag -a \"v%s\" -m \"%s\"", version, version)
          sh sprintf("git push --tags")
        } else {
          echo "No publishing branch"
        }
        
        sh "rm .npmrc"
      }
    }
}