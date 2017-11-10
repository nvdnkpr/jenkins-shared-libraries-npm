#!/usr/bin/groovy

def call(body){
  def toolList = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  toolList.each{ toolName, binPath -> 
    def toolRef = tool toolName

    env.PATH = "$toolRef/binPath:$PATH"
  }
}