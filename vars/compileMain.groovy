#!/usr/bin/groovy

def call(){
  // build main app
  sh "npm run build"
}