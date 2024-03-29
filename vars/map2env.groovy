#!/usr/bin/groovy

import com.cloudbees.groovy.cps.NonCPS

@NonCPS
def call(projectMap, projectPrefix = '', projectIgnoreProperties = []){
  // def envList = [:]
  def intoEnv
  intoEnv = { map, prefix, ignoreProperties ->
    map.each { key, value ->
      def upperPrefix = prefix.toUpperCase()
      def upperKey = key.toUpperCase()
      def envKey = "${upperPrefix}_${upperKey}"
      if (!ignoreProperties.contains(key)){
          switch(value){
            // value is a string, put it with the key into env
            case Boolean: case Number: case String:
              env[envKey] = value
              break
            // value is a array list, iterate over it, use index for differentiation
            case java.util.ArrayList:
              value.eachWithIndex{value2, idx -> env["${envKey}_${idx}"] = value2 }
              break
            // it's a sub object, work recursively
            default:
              intoEnv(value, "${prefix}_${key}", ignoreProperties)
          }
      }
    }
  }
  intoEnv(projectMap, projectPrefix, projectIgnoreProperties)
}
