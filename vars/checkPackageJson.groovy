#!/usr/bin/groovy

def call (){
  def packageFile = readFile('package.json')
  def packageJson = parseJson(packageFile)

  map2env(packageJson, 'package_json')


  // check if needed npm package scripts for a certain module type are defined
  generalTargets = ['lint', 'build', 'test']
  uiTargets = ['build.e2e', 'test.e2e', 'pack']

  scriptsMap = [
    "app": generalTargets + uiTargets,
    "feature": generalTargets + uiTargets,
    "ui": generalTargets + uiTargets,
    "connector": generalTargets,
    "client": generalTargets,
    "npm": generalTargets
  ]

  def repoModulType = env.REPO_NAME.split('-')[0]
  def possibleModuleTypes = scriptsMap.keySet() as List
  
  // get module type or 'npm' by default
  def moduleType = possibleModuleTypes.any{ it == repoModulType } ? repoModulType : 'npm'

  // get intersection of scripts in package.json and needed scripts from the scripts map
  def packageScripts = packageJson.scripts.keySet() as List
  def commonScripts = packageScripts.intersect(scriptsMap[moduleType])
  // if the sizes don't match we miss one more needed scripts
  if (commonScripts.size() != scriptsMap[moduleType].size()){
    def errorString = [
      "Needed package scripts for module type ${moduleType}: [${scriptsMap[moduleType]}]",
      "Found: [${commonScripts}]"
    ].join("\n")
      
    error errorString
  }

  // 
  (generalTargets + uiTargets).each{ target ->
    echo target
    env["HAS_${target.replace('.','_').toUpperCase()}"] = packageJson.scripts[target] != null
  }
  
  env.MODULE_TYPE = moduleType
}