#!/usr/bin/groovy

def call(){
  // build main app
  if (env.HAS_BUILD){
    sh "npm run build"
  }
}