def relativeJunitLogsPath = 'core-customize/hybris/log/junit'
def projectDir = '$WORKSPACE'
def platformHome = "${projectDir}/suite/hybris/bin/platform"

pipeline {

    libraries {
        lib("shared-library@${params.JENKINS_SCRIPT_BRANCH}")
    }

    agent {
        node {
            label 'master'
        }
    }

    options {
        skipDefaultCheckout(true) // No more 'Declarative: Checkout' stage
    }

    stages {
        stage('Platform Build') {
            tools {
                jdk 'sapmachine-jdk-17'
            }
            stages {

                stage('Prepare build name') {
                    steps{
                        script{
                            currentBuild.displayName = "${currentBuild.number}-${params.PROJECT_REPO_NAME}"
                            currentBuild.description = "Building ${params.PROJECT_TAG}"
                        }
                    }
                }

                stage('Checkout code') {
                    steps {
                        cleanWs()
                        script {
                            checkoutRepository("${projectDir}", "${params.PROJECT_TAG}", "${params.PROJECT_REPO}", "${params.PROJECT_REPO_NAME}")
                        }
                    }
                }

                stage('Extract suite') {
                    steps{
                        script {
                            extractCommerce(projectDir)
                        }
                    }
                }

                stage('Build suite') {
                    steps {
                        script {
                            executeCommerceBuild(projectDir, platformHome, "${params.PROJECT_REPO_NAME}")
                        }
                    }
                }

                stage('Run Tests') {
                    steps {
                        script {
                            echo "Running unit tests..."
                            executeAntTasks(platformHome, "${params.CI_TEST_TYPE} -Dtestclasses.suppress.junit.tenant=true -Dinclude.testclasses.all.custom.extensions=true", 'dev')
                        }
                    }
                }

                stage('Run SonarQube') {
                    steps {
                        script {
                            echo "Running SonarQube..."
                            def jacocoReportURL = "${WORKSPACE}/suite/hybris/log/${params.CI_TEST_TYPE}/jacoco-report/report.xml"
                            sonarqubeCheck(projectDir, platformHome, "${params.SONAR_PROJECT_NAME}", "${params.SONAR_URL}", jacocoReportURL)
                        }
                    }
                    post {
                        success {
                            publishCoverage adapters: [jacocoAdapter('**/jacoco-report/report.xml')]
                        }
                    }
                }
            }
        }
    }

    // post build actions
    post {
        always {
            emailNotification("${env.JOB_BASE_NAME}", "${currentBuild.number}", "${currentBuild.currentResult}", "${env.BUILD_URL}")
            failIfBuildUnstable()
        }
    }
}
