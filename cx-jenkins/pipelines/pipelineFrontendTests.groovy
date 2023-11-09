pipeline {

    agent {
        kubernetes {
            yaml '''
              apiVersion: v1
              kind: Pod
              spec:
                containers:
                - name: mvn-jdk-17-chrome
                  image: maven:3.8.7-sapmachine-17
                  command:
                  - sleep
                  args: 
                  - infinity
                - name: selenium-hub
                  image: selenium/hub:4.7.2
                  ports:
                    - containerPort: 4442
                    - containerPort: 4443
                    - containerPort: 4444
                  resources:
                    limits:
                      memory: "1000Mi"
                      cpu: ".5"
                  livenessProbe:
                    httpGet:
                      path: /wd/hub/status
                      port: 4444
                    initialDelaySeconds: 30
                    timeoutSeconds: 5
                  readinessProbe:
                    httpGet:
                      path: /wd/hub/status
                      port: 4444
                    initialDelaySeconds: 30
                    timeoutSeconds: 5  
                - name: selenium-chrome
                  image: selenium/node-chrome:4.7.2
                  env:
                      - name: SE_EVENT_BUS_HOST
                        value: "localhost"
                      - name: SE_EVENT_BUS_SUBSCRIBE_PORT
                        value: "4443"
                      - name: SE_EVENT_BUS_PUBLISH_PORT
                        value: "4442"
                  resources:
                    limits:
                      memory: "500Mi"
                      cpu: ".5"      
'''
        }
    }

    options {
        skipDefaultCheckout(true)
        //buildDiscarder(logRotator(numToKeepStr: '7', artifactNumToKeepStr: '7', artifactDaysToKeepStr: '15', daysToKeepStr: '15'))
    }

    parameters {
        string(
                name: 'tags',
                defaultValue: '',
                description: '''cucumber tags to run, use [Tag Expressions](https://cucumber.io/docs/cucumber/api/?lang=java#tags)''',
                trim: true
        )
    }

    stages {
        stage('Checkout code') {
            steps {
                cleanWs()
                script {
                    git(url: 'https://github.tools.sap/CONCUR-DIGITAL-STORE/store-automation.git', credentialsId: 'a06c8283-d34d-483f-a9c3-6c82059a4277', branch: 'main')
                }
            }
        }
        stage('Execute tests') {
            steps {
                script {
                    container('mvn-jdk-17-chrome') {
                        echo "Found cucumber tags to execute: ${params.tags.toString()}"
                        withMaven(globalMavenSettingsConfig: 'global-mvn-settings-xml', jdk: 'sapmachine-jdk-17', maven: 'mvn_3.8.7', mavenSettingsConfig: 'mvn-settings-xml') {
                            sh 'mvn -version'
                            sh 'java -version'
                            try {
                                if (params.tags.toString().isEmpty()) {
                                    sh "mvn clean test -Dspring.profiles.active=remote"
                                } else {
                                    sh "mvn clean test -Dcucumber.filter.tags=\"${params.tags}\" -Dspring.profiles.active=remote"
                                }
                            } catch (err) {
                                echo err.getMessage()
                            }
                        }
                        containerLog 'selenium-chrome'
                        containerLog 'mvn-jdk-17-chrome'
                    }
                }
            }
            post {
                always {
                    junit '**/surefire-reports/*.xml'
                    cucumber buildStatus: 'null', customCssFiles: '', customJsFiles: '', failedFeaturesNumber: -1, failedScenariosNumber: -1, failedStepsNumber: -1, fileIncludePattern: 'target/cucumber-reports/Cucumber.json', pendingStepsNumber: -1, skippedStepsNumber: -1, sortingMethod: 'ALPHABETICAL', undefinedStepsNumber: -1
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'reports/extentreports', reportFiles: 'extent-html.html', reportName: 'Extent Report', reportTitles: 'Extent Report', useWrapperFileDirectly: true])
                }
            }
        }
        stage('Zip reports') {
            steps {
                script {
                    zip archive: true, dir: 'reports/extentreports', exclude: '', glob: '', zipFile: 'reports.zip'
                }
            }
        }
        stage('Email reports') {
            steps {
                script {
                    jobName = "${env.JOB_BASE_NAME}"
                    buildNumber = "${currentBuild.number}"
                    buildResult = "${currentBuild.currentResult}"
                    buildURL = "${env.BUILD_URL}"
                    color = "${buildResult}"  != "SUCCESS" ? "${buildResult}"  == "UNSTABLE" ? "yellow" : "red" : "green"
                    subject = """Reports Jaas Job: ${jobName} [${buildNumber}]: ${buildResult}"""
                    body = """<p style='color: ${color}'><b>Job Name</b>: ${jobName} <br><b>Build Number</b>: [${buildNumber}] <br><b>Build Status</b>: ${buildResult}</p>
                                          <p>Check console output at &QUOT;<a href='${buildURL}'>${jobName} [${buildNumber}]</a>&QUOT;</p>"""
                    emailext (
                            subject: subject,
                            body: body,
                            recipientProviders: [developers(), requestor(), contributor(), culprits(), upstreamDevelopers()],
                            mimeType: 'text/html',
                            attachmentsPattern: 'reports/extentreports/extent-report.pdf'
                    )
                }
            }
        }
    }
}