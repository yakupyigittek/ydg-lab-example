pipeline {
  agent any

  options {
    timestamps()
    skipDefaultCheckout()
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Setup JDK') {
      steps {
        echo 'Using default JDK on agent'
      }
    }

    stage('Build & Test') {
      steps {
        // Windows ajanında Gradle Wrapper ile testleri çalıştır
        bat 'gradlew.bat clean test --no-daemon --console=plain'
      }
      post {
        always {
          // JUnit raporlarını arşivle (Gradle default yol)
          junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml'
          archiveArtifacts artifacts: 'build/reports/tests/test/**/*', allowEmptyArchive: true
        }
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
      // ...existing code...
    }
  }
}
