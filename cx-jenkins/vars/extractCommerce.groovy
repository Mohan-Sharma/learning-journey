def call(commerceDir) {
    echo "##### Extract commerce platform ##### -- ${commerceDir}"
    sh """
        #!/bin/bash
        mkdir ${commerceDir}/suite 
        chmod a+rwx ${commerceDir}/suite
        unzip -o $JENKINS_HOME/userContent/suite.zip -d ${commerceDir}/suite
       """
}