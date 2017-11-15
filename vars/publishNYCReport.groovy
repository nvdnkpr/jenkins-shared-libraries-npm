def call (){
  if (env.HAS_TEST_E2E.toBoolean()){
    publishHTML (
      target: [
        allowMissing: false,
        alwaysLinkToLastBuild: false,
        keepAll: true,
        reportDir: 'coverage',
        reportFiles: 'index.html',
        reportName: "NYC Report"
      ]
    )
  }
}
