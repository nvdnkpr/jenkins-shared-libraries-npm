#!/usr/bin/groovy

def call (){
  def packageFile = readFile('package.json')
  packageJson = parseJson(packageFile)

  map2env(packageJson, 'package_json')

  // check if needed npm package scripts for a certain module type are defined
  generalTargets = ['build', 'test']
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
  def commonScripts = packageJson.scripts.intersect(scriptsMap[moduleType])
  // if the sizes don't match we miss one more needed scripts
  if (commonScripts.size() !== scriptsMap[moduleType].size()){
    error [
      "Needed package scripts for module type ${moduleType}: [${scripts[moduleType]}]"
      "Found: [${commonScripts}]"
    ].join("\n")
  }

  // 
  (generalTargets + uiTargets).each{ target ->
    env["HAS_${target.replace('.','_').toUppercase()}"] = packageJson.scripts[target]
  }
  
  env.MODULE_TYPE = moduleType
}