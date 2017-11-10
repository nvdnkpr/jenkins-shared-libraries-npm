#!/usr/bin/groovy

def call(){
  env.BRANCH_FOLDER = "${env.MODULE_TYPE}/${env.PROJECT_NAME}/${env.REAL_BRANCH_NAME}"
  env.TARGET_FOLDER = env.IS_PULL_REQUEST.toBoolean()?
    "${env.STAGING_PULL_REQUEST_FOLDER_PREFIX}-${env.BUILD_NUMBER}" :
    "${env.STAGING_FEATURE_FOLDER_PREFIX}-${env.BUILD_NUMBER}"
  env.BASE_HREF = "${env.BRANCH_FOLDER}/${env.TARGET_FOLDER}"

  // build for e2e
  sh sprintf("npm run pack.e2e -- --base /%s/ --file %s", env.BASE_HREF, env.STAGING_E2E_PACKAGE)

  remoteTargetFolder = "${env.STAGING_FRONTEND_DOC_ROOT}/${env.BRANCH_FOLDER}"
  remoteFileName = "${env.TARGET_FOLDER}.zip"

  sh sprintf("mkdir -p %s", remoteTargetFolder)
  sh sprintf("cp %s %s", env.STAGING_E2E_PACKAGE, "${remoteTargetFolder}/${remoteFileName}")
  sh sprintf("unzip -o %s -d %s", "${remoteTargetFolder}/${remoteFileName}", "${remoteTargetFolder}/${env.TARGET_FOLDER}")

  //  publishing by SSH
  //  withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: env.CREDENTIALS_STAGING, passwordVariable: 'SSHPASS', usernameVariable: 'SSHUSER']]) {
  //    // create directory with branch name if not already existing
  //    sh "sshpass -e ssh -o StrictHostKeyChecking=no ${env.SSHUSER}@${env.STAGING_FE_HOST} \"mkdir -p /${remoteTargetFolder}\""
  //    // copy zip file to right folder and name
  //    sh "sshpass -e scp -o StrictHostKeyChecking=no -r ${env.E2E_PACKAGE} ${env.SSHUSER}@${env.STAGING_FE_HOST}:${remoteTargetFolder}/${remoteFileName}"
  //    // unzip zip to folder
  //    sh "sshpass -e ssh -o StrictHostKeyChecking=no ${env.SSHUSER}@${env.STAGING_FE_HOST} \"unzip -o ${remoteTargetFolder}/${remoteFileName} -d ${remoteTargetFolder}/${env.TARGET_FOLDER}\""
  //  }

  env.BASE_URL = "http://${env.STAGING_FRONTEND_HOST}/${env.BASE_HREF}/"
  echo "Build ${env.BUILD_NUMBER} deployed to ${env.BASE_URL}"
}