def call(commerceDir, branch, projectRepository, projectName) {
    urlPrefix = "https://"
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'a06c8283-d34d-483f-a9c3-6c82059a4277', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
        //complicating to prevent string interpolation
        def repoDomainPart = projectRepository.substring(urlPrefix.size())
        def repository = "https://$USERNAME:$PASSWORD@" + repoDomainPart
        def customCodeDir = commerceDir + '/' + projectName
        echo "##### Checkout repository #####"
        sh '''
            #!/bin/bash
            mkdir ''' + customCodeDir + '''
            chmod a+rwx ''' + customCodeDir + '''
            cd ''' + customCodeDir + ''' && git clone ''' + repository + ''' . && git fetch --all && git checkout origin/''' + branch
    }
}