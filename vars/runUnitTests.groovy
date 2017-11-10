#!/usr/bin/groovy

def call(){
  if (env.HAS_TEST.toBoolean()) {
    sh "npm test"
  } else {
    echo "No Unit Tests to run"
  }
}