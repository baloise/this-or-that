pipeline {
    agent {
        // run on any slave labeled as 'common'
        label 'common'
    }

    parameters {
        booleanParam(defaultValue: true, 
                     description: 'Flag whether quality checks should be performed.', 
                     name: 'RUN_QUALITY_CHECKS')
    }

    options {
        skipStagesAfterUnstable()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '28'))
        timeout(time: 1, unit: 'HOURS')
    }

    triggers {
        // at least once a day (dependencies might change over time)
        cron('H H(0-7) * * *')
        // check every fifteen minutes for changes
        pollSCM('H/15 * * * *')
    }

    stages {
        stage("Preparation") {
            steps {
                script {
                    currentBuild.displayName += " - ${params.RUN_QUALITY_CHECKS}"
                }
            }
        }

        stage("SCM checkout") {
            steps {
                deleteDir()
                checkout scm
                script {
                    pomInfo = readMavenPom file: 'pom.xml'
                    currentBuild.description = "${pomInfo.version}"
                }
            }
        }

        stage("CI build") {
            steps {
                mavenbuild uploadArtifactsWithBranchnameInVersion: true,
                           mavenArgs: "-DcreateChecksum=true -Dmaven.javadoc.skip=true"
            }
        }

        stage("Quality assurance") {
            when {
                expression { return params.RUN_QUALITY_CHECKS }
            }

            steps {
                sonar()
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: '**/target/*-reports/TEST*.xml'
        }

        fixed {
            mailTo status: "SUCCESS", actuator: true, recipients: [], logExtract: true
        }

        failure {
            mailTo status: "FAILURE", actuator: true, recipients: [], logExtract: true
        }
    }
}
