def pipelineRepo = 'https://github.tools.sap/I504180/concur-jenkins'
def projectRepoName = 'concur-hybris'
def projectRepo = 'https://github.tools.sap/CONCUR-DIGITAL-STORE/' + projectRepoName
def projectTag = '${GIT_BRANCH}'
def packageToTest = 'com.sap.concur.*'
def sonarUrl = 'https://sonarce.wdf.sap.corp/sonar'
def sonarProjectName = 'CONCUR-HYBRIS'
def environment = '${ENVIRONMENT_ID}'
def ciTestType = 'pipelineunittests'

// ****************************
// *** JOB PARAMETERS
// ****************************
class JobParameters {

    static void setLogs(job) {
        job.with {
            //logRotator(int daysToKeep, int numToKeep, int artifactDaysToKeep, int artifactNumToKeep)
            logRotator(15, 7, 15, 7)
        }
    }

    static void setLibraryBranchParam(job) {
        job.with {
            parameters {
                stringParam('JENKINS_SCRIPT_BRANCH', 'TARDIS', 'Pipeline scripts branch')
            }
        }
    }

    static void setProjectRepository(job, projectRepo) {
        job.with {
            parameters {
                stringParam('PROJECT_REPO', projectRepo, 'Concur Hybris repository url')
            }
        }
    }

    static void setProjectTag(job, projectTag) {
        job.with {
            parameters {
                stringParam('PROJECT_TAG', projectTag, 'Concur Hybris branch to build')
            }
        }
    }

    static void setProjectName(job, projectRepoName) {
        job.with {
            parameters {
                stringParam('PROJECT_REPO_NAME', projectRepoName, 'Repository name of custom hybris code')
            }
        }
    }

    static void setSonarUrl(job, sonarUrl) {
        job.with {
            parameters {
                stringParam('SONAR_URL', sonarUrl, 'Sonar Url')
            }
        }
    }

    static void setSonarProjectName(job, sonarProjectName) {
        job.with {
            parameters {
                stringParam('SONAR_PROJECT_NAME',sonarProjectName, 'Sonar project name')
            }
        }
    }

    static void setCiTestType(job, ciTestType) {
        job.with {
            parameters {
                stringParam('CI_TEST_TYPE', ciTestType, 'Type of tests to run')
            }
        }
    }

    static void setPackageToTest(job, packageToTest) {
        job.with {
            parameters {
                stringParam('PACKAGE_TO_TEST', packageToTest, 'Package(s) to test')
            }
        }
    }

    static void setDatabaseUpdateMode(job) {
        job.with {
            parameters {
                choiceParam('DB_UPDATE_MODE', ['NONE', 'UPDATE', 'INITIALIZE'], 'Possible options for databaseUpdateMode are NONE, UPDATE, and INITIALIZE')
            }
        }
    }

    static void setEnvironment(job, environment) {
        job.with {
            parameters {
                choiceParam('ENVIRONMENT_ID', ['d1', 'd2', 's1'], 'The environment ID to deploy to')
            }
        }
    }

    static void setStrategy(job) {
        job.with {
            parameters {
                choiceParam('DEPLOY_STRATEGY', ['ROLLING_UPDATE', 'RECREATE'], 'Deployment strategy (ROLLING_UPDATE or RECREATE)')
            }
        }
    }
}

// ****************************
// *** JOB DEFINITION
// ****************************

def buildCommerce = pipelineJob('build-commerce') {

    properties {
        githubProjectUrl("${projectRepo}")
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url("${pipelineRepo}")
                        credentials("mohan-github-tools")
                    }
                    branch('${JENKINS_SCRIPT_BRANCH}')
                }
                scriptPath('pipelines/pipelineBuildEveryDay.groovy')
                lightweight(false)
            }
        }
    }
    triggers {
        githubPullRequest {
            admin('I504180')
            triggerPhrase('.*(re)?build.*')
            useGitHubHooks()
            permitAll()
            displayBuildErrorsOnDownstreamBuilds()
            extensions {
                commitStatus {
                    context('Jenkins')
                    completedStatus('SUCCESS', 'All is well')
                    completedStatus('FAILURE', 'Something went wrong. Investigate!')
                    completedStatus('ERROR', 'Something went really wrong. Investigate!')
                }
                buildStatus {
                    completedStatus('SUCCESS', 'There were no errors, go have a cup of coffee...')
                    completedStatus('FAILURE', 'There were errors, for info, please see...')
                    completedStatus('ERROR', 'There was an error in the infrastructure, please contact...')
                }
            }
        }
    }
}

JobParameters.setLogs(buildCommerce)
JobParameters.setLibraryBranchParam(buildCommerce)
JobParameters.setProjectRepository(buildCommerce, projectRepo)
JobParameters.setProjectTag(buildCommerce, projectTag)
JobParameters.setProjectName(buildCommerce, projectRepoName)
JobParameters.setCiTestType(buildCommerce, ciTestType)
JobParameters.setPackageToTest(buildCommerce, packageToTest)
JobParameters.setSonarUrl(buildCommerce, sonarUrl)
JobParameters.setSonarProjectName(buildCommerce, sonarProjectName)

def packageAndDeploy = pipelineJob('package-and-deploy') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url("${pipelineRepo}")
                        credentials("mohan-github-tools")
                    }
                    branch('${JENKINS_SCRIPT_BRANCH}')
                }
                scriptPath('pipelines/pipelinePackageAndDeploy.groovy')
                lightweight(false)
            }
        }
    }
}


JobParameters.setLogs(packageAndDeploy)
JobParameters.setLibraryBranchParam(packageAndDeploy)
JobParameters.setProjectTag(packageAndDeploy, projectTag)
JobParameters.setDatabaseUpdateMode(packageAndDeploy)
JobParameters.setEnvironment(packageAndDeploy, environment)
JobParameters.setStrategy(packageAndDeploy)
JobParameters.setProjectRepository(packageAndDeploy, projectRepo)
JobParameters.setProjectName(packageAndDeploy, projectRepoName)

def bddFrontendTests = pipelineJob('frontend-tests-on-demand') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url("${pipelineRepo}")
                        credentials("mohan-github-tools")
                    }
                    branch('${JENKINS_SCRIPT_BRANCH}')
                }
                scriptPath('pipelines/pipelineFrontendTests.groovy')
                lightweight(false)
            }
        }
    }
}

JobParameters.setLogs(bddFrontendTests)
JobParameters.setLibraryBranchParam(bddFrontendTests)

// ****************************
// *** PIPELINE LIST VIEW DEFINITION
// ****************************

listView('Commerce Pipelines') {
    description('All hybris build and deploy jobs')
    jobs {
        names(
                'build-commerce',
                'package-and-deploy',
                'frontend-tests-on-demand'
        )
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}
