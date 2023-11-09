def call(platformHome, antTasks, env, antOptions = "-Xmx512m -Dfile.encoding=UTF-8") {
        echo "##### Execute ant tasks : ${antTasks} #####"
        sh "cd ${platformHome} && . ./setantenv.sh && export ANT_OPTS=\"${antOptions}\" && ant ${antTasks} -Denv=${env}"
} 