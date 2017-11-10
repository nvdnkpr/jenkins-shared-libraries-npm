#!/usr/bin/groovy

def call(name, email){
  // Set Git Email and Name
  sh "git config user.name $name"
  sh "git config user.email $email"
}
