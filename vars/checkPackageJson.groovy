#!/usr/bin/groovy

import groovy.json.JsonOutput


def call (){
  def packageFile = readFile('package.json')
  def packageJson = parseJson(packageFile)

  map2env(packageJson, 'package_json', ['dependencies', 'devDependencies','nyc'])


  // check if needed npm package scripts for a certain module type are defined
  codeTargets = ['lint']
  generalTargets = ['build', 'test']
  uiTargets = ['build.e2e', 'test.e2e', 'pack']

  scriptsMap = [
    "app": codeTargets + generalTargets + uiTargets,
    "feature": codeTargets + generalTargets + uiTargets,
    "ui": codeTargets + generalTargets + uiTargets,
    "connector": codeTargets + generalTargets,
    "client": codeTargets + generalTargets,
    "npm": generalTargets,
    "config": []
  ]

  def repoModulType = env.REPO_NAME.split('-')[0]
  def possibleModuleTypes = scriptsMap.keySet() as List
  
  // get module type or 'npm' by default
  def moduleType = possibleModuleTypes.any{ it == repoModulType } ? repoModulType : 'npm'

  // get intersection of scripts in package.json and needed scripts from the scripts map
  if (packageJson.scripts != null){
    def packageScripts = packageJson.scripts.keySet() as List
    def commonScripts = packageScripts.intersect(scriptsMap[moduleType])

    // if the sizes don't match we miss one more needed scripts
    if (commonScripts.size() < scriptsMap[moduleType].size()){
      def errorString = [
        "Needed package scripts for module type ${moduleType}: [${scriptsMap[moduleType]}]",
        "Found: [${commonScripts}]"
      ].join("\n")
        
      error errorString
    }
  }

  // set boolean env variables to specify if a certain task exists as script
  if (packageJson.scripts == null){
    packageJson.scripts = [:]
  }
  
  (codeTargets + generalTargets + uiTargets).each{ 
    target -> env["HAS_${target.replace('.','_').toUpperCase()}"] = packageJson.scripts[target] != null
  }
  
  // add/change certain packages only for the CI Server
    // def additionalPackages = ["semantic-release"]; // "eslint-configuration-vmt-ci"
    // additionalPackages.each{ packageName -> packageJson.devDependencies[packageName] = "latest" }
    
  // add/change certain scripts only for the CI Server
    // packageJson.scripts["semantic-release"] = "semantic-release pre && npm publish && semantic-release post"

    if (packageJson.scripts["test"] != null){
      packageJson.scripts["test"] = "JUNIT_REPORT_PATH=./junit/report.xml " + packageJson.scripts["test"]
    }
    // script "lint": add missing or replace existing lint, add a lint configuration that applies to the CI Server env

  // add/change certain properties
    // add publishConfig
    packageJson.publishConfig = [:]
    packageJson.publishConfig["registry"] = env.PUBLISHING_REGISTRY

    // add/modify author
    packageJson.author = "${env.PUBLISHING_PUBLISHER_NAME} <${env.PUBLISHING_PUBLISHER_EMAIL}>"

    // add/modify license
    packageJson.license = "${env.PUBLISHING_LICENSE}"
    
    // e.g. for semantic-release under the "release" property
    // packageJson["release"] = {}
    
    // e.g. for nyc under the "nyc" property since this can have been changed 
    // packageJson["nyc"] = {
      // "extends" : "@vmt/nyc-config"
    // }


  //   
  def json = JsonOutput.prettyPrint(JsonOutput.toJson(packageJson))
  writeFile file: 'package.json', text: json


  
  env.MODULE_TYPE = moduleType
}
