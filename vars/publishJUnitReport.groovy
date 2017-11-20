def call (){
  if (env.HAS_TEST.toBoolean()){
    junit '**/junit/*.xml'
  }
}
