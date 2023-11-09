def call(commerceDir, platformHome, projectName, sonarUrl, jacocoReportURL) {

    withCredentials([string(credentialsId: 'sonarce-concur-hybris', variable: 'SECRET')]) {
        echo "Jacoco Report URL: $jacocoReportURL"
        addProperty(commerceDir, "sonar.login=${SECRET}")
        addProperty(commerceDir, "sonar.host.url=$sonarUrl")
        addProperty(commerceDir, "sonar.projectName=$projectName")
        addProperty(commerceDir, "sonar.projectKey=$projectName")
        addProperty(commerceDir, "sonar.coverage.jacoco.xmlReportPaths=$jacocoReportURL")
    }


    withSonarQubeEnv('SAPSonarQubeCE') {
        executeAntTasks(platformHome, "sonarcheck", "dev", "-Xms256m -Xmx4096m -XX:+UseSerialGC -Dfile.encoding=UTF-8")
    }

    echo "Sonar Quality Gate check in progress..."
    script {
        SONAR_CHECK_URL = "$sonarUrl/api/qualitygates/project_status?projectKey"
        output = sh(returnStdout: true, script: "echo \$(curl $SONAR_CHECK_URL | jq '.projectStatus.status')")
        while (output.contains("NONE")) {
            sh('sleep 10s')
            output = sh(returnStdout: true, script: "echo \$(curl $SONAR_CHECK_URL | jq '.projectStatus.status')")
        }
        if (output.contains("ERROR")) {
            currentBuild.result = 'UNSTABLE'
            echo 'Build: SUCCESSFUL!\nSonar Quality Gate: FAILED!\nFinal result: UNSTABLE!'
        }
    }
}