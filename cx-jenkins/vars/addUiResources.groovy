def call(commerceDir, projectRepository, tagName, projectName) {
    def now = new Date().format("yyyyMMddHHmm", TimeZone.getTimeZone('UTC'))
    def urlPrefix = "https://"
    def repoDomainPart = projectRepository.substring(urlPrefix.size())
    def customCodeDir = commerceDir + '/' + projectName
    tagName = tagName + "-" + now
    echo "##### Pushing to tag: ${tagName} #####"
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'a06c8283-d34d-483f-a9c3-6c82059a4277', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
        //complicating to prevent string interpolation
        def repository = "https://$USERNAME:\$PASSWORD@" + repoDomainPart
        sh '''
            #!/bin/bash
            echo "##### Whitelisting _ui resources to commit to ccv2 branch #####"
            cd ''' + customCodeDir + '''
            sed -i 's/**\\/_ui\\/addons//g' .gitignore
            sed -i 's/**\\/_ui-src\\/addons//g' .gitignore
            sed -i 's/**\\/_ui\\///g' .gitignore
            git commit .gitignore -m "versioning _ui resources"
            
            echo "##### Building _ui resources #####"
            cd ''' + commerceDir + '''/suite/hybris/bin/platform && . ./setantenv.sh && ant -f ''' + commerceDir + '''/suite/build.xml prepare customize sassclean sasscompile clean all production -Denvironment=local -Dproduction.legacy.mode=true
            
            echo "##### Committing _ui resources #####"
            cd ''' + commerceDir + '''/suite/hybris/bin/custom/concur/concurstorefront/web/webroot/_ui/
            git add .
            git commit -m "adding all _ui resources"
            
            git tag ''' + tagName + ''' -a -m "ccv2 release"
            cd ''' + customCodeDir + ''' && git push ''' + repository + ''' --tags
        '''
        return tagName;
    }
}