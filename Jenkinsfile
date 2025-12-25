pipeline {
  agent any

  options {
    timestamps()
    ansiColor('xterm')
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Setup JDK') {
      steps {
        // Eğer Jenkins'te JDK kurulu değilse, burayı kendi ortamınıza göre uyarlayın
        // withEnv gibi ek JAVA_HOME ayarları da yapılabilir.
        echo 'Using default JDK on agent'
      }
    }

    stage('Build & Test') {
      steps {
        // Windows ajanında Gradle Wrapper ile testleri çalıştır
        // PowerShell shell'inde wrapper .bat kullanılmalı
        bat 'gradlew.bat clean test --no-daemon --console=plain'
      }
    }
  }

  post {
    success {
      echo 'Tests passed. Pipeline succeeded.'
    }
    unstable {
      echo 'Build is unstable.'
    }
    failure {
      echo 'Tests failed. Pipeline failed.'
    }
    always {
      // JUnit raporlarını arşivle (Gradle default yol)
      junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml'
      archiveArtifacts artifacts: 'build/reports/tests/test/**/*', allowEmptyArchive: true
    }
  }
}

