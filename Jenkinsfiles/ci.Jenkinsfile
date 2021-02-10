#!groovy
@Library("jenkinslib@master") _

String buildType = "${env.buildType}"
String buildShell = "${env.buildShell}"

def build = new org.devops.build()

pipeline {
    agent any

    stages {
        stage("Build"){
            steps{
                script{
                    build.build(buildType,buildShell)
                }
            }
        }
    }
}
