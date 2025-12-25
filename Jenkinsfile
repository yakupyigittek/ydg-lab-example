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
      when {
        expression {
          def b = (env.BRANCH_NAME ?: env.GIT_BRANCH ?: '')
          return b == 'main' || b == 'origin/main' || b.endsWith('/main')
        }
      }
      steps {
        // Windows ajanında Gradle Wrapper ile testleri çalıştır
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
