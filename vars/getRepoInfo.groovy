#!/usr/bin/groovy

def call(){
  def remoteUrl = scm.getUserRemoteConfigs()[0].getUrl()
  def strippedUrl = remoteUrl.tokenize(':')[1].replace(".git","")
  def tokenizedRepo = strippedUrl.tokenize('/')

  env.REMOTE_URL = remoteUrl
  env.PROJECT_NAME = tokenizedRepo[0]
  env.REPO_NAME = tokenizedRepo[1]
  env.REPO_SLUG = strippedUrl
}
