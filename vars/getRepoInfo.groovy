#!/usr/bin/groovy

def call(){
  def remoteUrl = scm.getUserRemoteConfigs()[0].getUrl()
  def tokenizedRepo = remoteUrl.replace(/\.git/,"").tokenize('/')

  def projectName = tokenizedRepo[2]
  def repoName = tokenizedRepo[3]
  def repoSlug = "$projectName/$repoName"

  env.REMOTE_URL = remoteUrl
  env.PROJECT_NAME = projectName
  env.REPO_NAME = repoName
  env.REPO_SLUG = repoSlug
}