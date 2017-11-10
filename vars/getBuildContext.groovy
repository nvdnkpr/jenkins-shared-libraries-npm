#!/usr/bin/groovy

def call(){
  // check if we have a pull request, inject result into env
  def isPullRequest = (env.BRANCH_NAME[0..2] == 'PR-')
  env.IS_PULL_REQUEST = isPullRequest
  // ... if we have, retrieve the ID and inject it into env either
  if (isPullRequest) {
    env.PULL_REQUEST_ID = env.BRANCH_NAME.split('-')[1]
    
    def pullRequestInfoUrl = sprintf(env.GIT_PROVIDER_API_PULLREQUEST, env.GIT_PROVIDER_API_BASE_URL, env.PULL_REQUEST_ID)
    def pullRequestData = callAPI(pullRequestInfoUrl, env.MAIN_CREDENTIALS_GIT_PROVIDER)

    def sourceBranchName = pullRequestData.source.repository.full_name
    def destBranchName = pullRequestData.destination.repository.full_name

    currentBuild.displayName = "${sourceBranchName} -> ${destBranchName} (Build ${env.BUILD_NUMBER})"

    env.REAL_BRANCH_NAME = sourceBranchName
  } else {
    env.REAL_BRANCH_NAME = env.BRANCH_NAME
  }

}