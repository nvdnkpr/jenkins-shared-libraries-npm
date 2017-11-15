def call (){
  if (env.HAS_TEST){
    junit './junit/*.xml'
  }
}
