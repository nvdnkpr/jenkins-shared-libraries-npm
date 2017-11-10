#!/usr/bin/groovy

import com.cloudbees.groovy.cps.NonCPS
import groovy.json.JsonSlurperClassic


@NonCPS
def call(String jsonText){
  final slurper = new JsonSlurperClassic()
  return new HashMap<>(slurper.parseText(jsonText))
}
