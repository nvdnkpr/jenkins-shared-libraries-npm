#!/usr/bin/groovy

def call(){
  // Read main config
  mainConfig = configFileReader('main', 'main', true)
  // Read stage configurations
  mainConfig.configurations.each { prefix, fileId -> configFileReader(fileId, prefix, true)
  }
}