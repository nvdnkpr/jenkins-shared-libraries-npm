#!/usr/bin/groovy

def call(String configFileId, String prefix = '', boolean injectIntoEnv = false) {
  configFileProvider([configFile(fileId: configFileId, variable: 'configFile')]) {
    def config = readFile(configFile)
    def json = parseJson(config)
    
    if(injectIntoEnv){
      map2env(json,'main')
    }
    
    return json
  }

}