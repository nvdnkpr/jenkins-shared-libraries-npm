#!/usr/bin/groovy

def call(){
  // TODO: use a parameter to run specific suits of e2e tests, current command runs all suites
  sh sprintf("npm run test.e2e -- --seleniumAddress %s --baseUrl %s", env.STAGING_SELENIUM_SERVER, env.BASE_URL)
}