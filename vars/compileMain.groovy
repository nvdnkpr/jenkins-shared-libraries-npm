#!/usr/bin/groovy

def call(){
  // build main app
  if (env.HAS_BUILD.toBoolean()){
    sh "npm run build"
  }
}