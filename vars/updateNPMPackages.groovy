#!/usr/bin/groovy

def call (){
  withNPM(npmrcConfig: env.PUBLISHING_NPMRC_CONFIG) {
    sh "npm prune"
    sh "npm update"
    sh "npm install"
  }
}