pipeline {
    agent any

    tools {
        maven 'Maven-3.9.5'  // Match the name from Global Tool Configuration
    }

    // environment {
    //     // Define environment variables that will be used across stages
    //     DOCKER_REGISTRY_URL = credentials('docker-registry-url') // Stored in Jenkins credentials
    //     DOCKER_IMAGE_TAG = "${env.BUILD_ID}"
    //     DOCKER_CRED_ID = 'docker-credentials' // Jenkins credentials ID for Docker registry
    //     BRANCH_NAME = "jenkins-update-${env.BUILD_ID}"
    //     NEW_TAG = "${env.BUILD_ID}"
    //     APP_NAME = "microservice1"
    // }

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


        // Static Code Analysis with SonarQube
        stage('Scan') {
            steps {
                withSonarQubeEnv(installationName: 'sq1') {
                    // sh 'mvn clean org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar' // For Linux agent.
                    bat 'mvn clean verify sonar:sonar'
                }
            }
        }

        stage("Quality Gate") {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // // Build Docker Image & push to our artifactory
        // stage('Build Docker Image') {
        //     steps {
        //         script {
        //             // Build the image using the Dockerfile in the current directory
        //             // The "docker" variable is provided by the Docker Pipeline plugin
        //             dockerImage = docker.build "${DOCKER_REGISTRY_URL}:${DOCKER_IMAGE_TAG}"
        //         }
        //     }
        // }

        // stage('Push to Artifact Store') {
        //     steps {
        //         script {
        //             // Wrap the push operation with credentials for authentication
        //             docker.withRegistry('https://index.docker.io/v1/', DOCKER_CRED_ID) {
        //                 dockerImage.push()
        //             }
        //         }
        //     }
        // }

        // stage('Cleanup') {
        //     steps {
        //         // Optionally remove the local image to save space
        //         sh "docker rmi ${DOCKER_REGISTRY_URL}:${DOCKER_IMAGE_TAG}"
        //     }
        // }

        // // Update gitops branch
        // stage('Prepare gitops Update') {
        //     steps {
        //         sh """
        //             # Create new branch
        //             git checkout -b ${BRANCH_NAME}
        //         """
        //     }
        // }

        // stage('Update Helm Values') {
        //     steps {
        //         sh """
        //             # Update Helm image tag using yq
        //             yq -i '.image.tag = "${NEW_TAG}"' values.yaml
        //         """
        //     }
        // }

        // stage('Create GitOps Pull Request') {
        //     steps {
        //         sh """
        //             # Configure Git user (machine user)
        //             git config user.email "jenkins-bot@company.com"
        //             git config user.name "Jenkins CI"

        //             # Commit changes
        //             git add .
        //             git commit -m "Update ${APP_NAME} image tag to ${NEW_TAG}"

        //             # Push branch
        //             git push origin ${BRANCH_NAME}

        //             # Create PR using GitHub CLI
        //             gh pr create \
        //                 --title "Deploy ${APP_NAME} version ${NEW_TAG}" \
        //                 --body "Automated update of ${APP_NAME} image tag to ${NEW_TAG} by Jenkins pipeline." \
        //                 --base main \
        //                 --head ${BRANCH_NAME}
        //         """
        //     }
        // }
    }

    // post {
    //     always {
    //         cleanWs() // Clean workspace after build
    //     }
    //     success {
    //         echo "Pipeline completed successfully!"
    //     }
    //     failure {
    //         echo "Pipeline failed!"
    //         // Add notification here (email, ... etc.)
    //     }
    // }
}