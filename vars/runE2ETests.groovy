#!/usr/bin/groovy

def call(){
  if (env.HAS_TEST_E2E.toBoolean()) {
    // TODO: use a parameter to run specific suits of e2e tests, current command runs all suites
    sh "npm run test.e2e -- --seleniumAddress $STAGING_SELENIUM_SERVER --baseUrl $BASE_URL"
  } else {
    echo "No E2E Tests to run"
  }
}