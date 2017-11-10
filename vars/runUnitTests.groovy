#!/usr/bin/groovy

def call(){
  if (env.HAS_TEST) {
    sh "npm test"
  }
}