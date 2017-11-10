#!/usr/bin/groovy

def call(url, auth){
  def response = httpRequest url: url, authentication: auth,
    consoleLogResponseBody: false,
    contentType: 'APPLICATION_JSON',
    acceptType: 'APPLICATION_JSON'

  writeFile file: 'response.json', text: response.content

  def responseBody = readFile("response.json")
  def responseJson = parseJson(responseBody)
  sh "rm -f response.json"

  return responseJson
}