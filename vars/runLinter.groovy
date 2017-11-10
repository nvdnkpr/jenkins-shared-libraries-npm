#!/usr/bin/groovy

def call(){
  // TODO: run with slightly different rules and options on build system: 
  // some rules will be now error and warnings don't stop the build but mark it unstable

  // TODO: either add own script in package scripts or run linting manually here to be sure no one 'cheated linting'
  sh "npm run tslint"
}