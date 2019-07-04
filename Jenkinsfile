pipeline {
  agent any
  stages {
    stage('error') {
      agent any
      steps {
        sh 'cd EasyRMT'
        ws(dir: 'EasyRMT') {
          sh 'ls'
        }

      }
    }
  }
}