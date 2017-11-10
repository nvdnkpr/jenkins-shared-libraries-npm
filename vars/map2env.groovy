#!/usr/bin/groovy

import com.cloudbees.groovy.cps.NonCPS

@NonCPS
def call(projectMap, projectPrefix = ''){
  def injectIntoEnv(map, prefix){
    map.each { k, v ->
      def envKey = "${prefix.toUpperCase()}_${k.toUpperCase()}" 
      switch(v){
          // value is a string, put it with the key into env
          case String:
              env[envKey] = v
              break
          // value is a array list, iterate over it, use index for differentiation
          case java.util.ArrayList:
              v.eachWithIndex{v2, i -> env["${envKey}_${i}"] = v2 }
              break
          // it's a sub object, work recursively
          default:
              injectIntoEnv(v, "${prefix}_${k}")
      }
    }
  }

  injectIntoEnv(projectMap, projectPrefix)
}
