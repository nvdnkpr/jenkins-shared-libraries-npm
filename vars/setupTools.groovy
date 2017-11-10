#!/usr/bin/groovy

def call(body){
  // def toolList = [:]
  // body.resolveStrategy = Closure.DELEGATE_FIRST
  // body.delegate = config
  // body()

  def nodeJs = tool "NodeJS 8.7.0"
  def sonarScanner = tool "SonarQube Scanner 3.0"

  env.PATH = "$nodeJs/bin:$sonarScanner/bin:$PATH"
}