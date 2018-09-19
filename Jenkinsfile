pipeline {
    agent {
        label 'common'
    }

    tools {
        jdk 'JDK_1_8'
    }

    options {
        skipStagesAfterUnstable()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '28'))
        timeout(time: 1, unit: 'HOURS')
    }

    triggers {
        // at least once a day
        cron('H H(0-7) * * *')
        // check every sixty minutes for changes
        pollSCM('H/60 * * * *')
    }

    stages {
        stage("SCM Checkout") {
            steps {
                deleteDir()
                checkout scm
            }
        }

        stage("Maven") {
            steps {
                mavenbuild()
            }
        }
    }
}
