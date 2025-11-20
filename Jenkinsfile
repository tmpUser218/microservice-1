pipeline {
    agent any

    tools {
        maven 'Maven-3.9.5'  // Match the name from Global Tool Configuration
    }

    stages {
        // Apply formatting before linting to avoid any errors.
        stage('Format Code') {
            steps {
                bat 'mvn spotless:apply'
            }
        }

        stage('Lint') {
            steps {
                // sh './mvnw spotless:check' This works for Jenkins on Linux agent.
                bat 'mvn spotless:check'
            }
        }

        stage('Build') {
            steps {
                // sh './mvnw clean package' This works for Jenkins on Linux agent.
                bat 'mvn clean package'
            }
        }

        stage('Unit Tests') {
            steps {
                bat 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Integration Tests') {
            steps {
                bat 'mvn verify -DskipUnitTests'
            }
            post {
                always {
                    junit 'target/failsafe-reports/*.xml'
                }
            }
        }

        stage('Code Coverage') {
            steps {
                bat 'mvn jacoco:report'
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/site/jacoco',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Code Coverage'
                ])
            }
        }
    }
}