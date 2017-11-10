#!/usr/bin/groovy

def call(){
  // Read main config
  mainConfig = configFileReader('main', '', true)
  // Read stage configurations
  mainConfig.configurations.each { fileId -> configFileReader(fileId, "${fileId.replace('-','_')}", true) }
}