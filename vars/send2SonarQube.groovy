#!/usr/bin/groovy

def call(){
  echo "We could send data to SonarQube"
  //
  //  def name = env.JOB_NAME
  //  def key = name.replaceAll('[^\\w\\d-_:\\. ]', '_')
  //  def version = env.PROJECT_VERSION
  //
  //  // TODO: get this from the config file: build-runner
  //  sonar_host_url = 'http://sapp-dev-ci-sonar-01:9000/'
  //
  //
  //
  //  cmd = "${homeDir}/bin/sonar-scanner"
  //  cmd += " -Dsonar.host.url=${sonar_host_url}"
  //  cmd += " -Dsonar.projectKey=$key"
  //  cmd += " -Dsonar.projectName=\"$name\""
  //  cmd += " -Dsonar.projectVersion=$version"
  //  cmd += ' -Dsonar.sources=src'
  //  cmd += ' -Dsonar.ts.tslintpath=./node_modules/tslint/bin/tslint'
  //  cmd += ' -Dsonar.ts.lcov.reportpath=./coverage/lcov.info'
  //  cmd += ' -Dsonar.ts.ignoreNotFound=true'
  //
  //  sh cmd
}