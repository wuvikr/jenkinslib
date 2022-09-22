#!groovy
@Library('sharable-lib-github') _
import org.devops.*

def tools = new Tools()
def git = new Git()
def build = new Build()
def sast = new Sast()
def artifact = new Artifacts()

//Pipeline
pipeline {
    agent { label 'build01' }
    
    options {
        timestamps()                        //日志会有时间
        skipDefaultCheckout()               //删除隐式checkout scm语句
        disableConcurrentBuilds()           //禁止并行
        timeout(time: 1, unit: 'HOURS')     //流水线超时设置1h
        ansiColor('xterm')

        // 只保留最近5次构建记录
        buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')
    }

    parameters {
        string description: '请填写项目仓库地址：', name: 'gitURL', defaultValue: 'http://gitlab.wuvikr.top/mytest/webdemo.git'
        string description: '请填写项目分支：', name: 'branchName', defaultValue: 'main'
        booleanParam description: '是否使用sonar进行代码扫描？（默认：false）', name: 'sonarScan'
    }

    stages {
        // 下载代码
        stage("GetCode") {
            steps {
                timeout(time:5, unit:"MINUTES") {
                    script {
                        tools.PrintMes("获取代码!","green")
                        git.GetCode(gitURL,branchName)
                    }
                }
            }
        }

        // 定义全局变量
        stage("define var") {
            steps {
                script {
                    env.buName = "${env.JOB_BASE_NAME}".split("-")[0]
                    env.serviceName = "${env.JOB_BASE_NAME}".split("-")[1]
                    env.commitID = sh(returnStdout: true, script: "git rev-parse HEAD").trim().take(7)
                }
            }
        }
        
        // 构建
        stage("Build"){
            steps {
                timeout(time:20, unit:"MINUTES") {
                    script {
                        tools.PrintMes("应用打包!","green")
                        build.CodeBuild("maven")
                    }
                }
            }
        }
        
        //代码扫描
        stage("CodeScan"){
            when { environment name: 'sonarScan', value: 'true' }
            steps {
                timeout(time:20, unit:"MINUTES") {
                    script {
                        tools.PrintMes("代码扫描!","green")
                        sast.SonarScanner()
                    }
                }
            }
        }

        //上传制品
        stage("uploadArtifact") {
            steps {
                script {
                    tools.PrintMes("上传制品!","green")
                    artifact.UploadArtifact()
                }
                
            }
        }

    }

    //构建后操作
    post {
        always {
            script {
                println("delete Dir")
                //deleteDir()
                currentBuild.displayName = "${env.serviceName}-${env.commitID}-${env.BUILD_NUMBER}"
                
            }
        }

        success {
            script {
                wrap([$class: 'BuildUser']){
                    //echo "full name is $BUILD_USER"
                    //echo "user id is $BUILD_USER_ID"
                    //echo "user email is $BUILD_USER_EMAIL"
                    //echo "user group is $BUILD_USER_GROUPS"
                    currentBuild.description = "构建成功! \nbuildUser: ${BUILD_USER} \nbranchName: ${params.branchName}" 
                }
            }
        }

        failure {
            script {
                currentBuild.description = "\n 构建失败!" 
            }
        }

        aborted {
            script {
                currentBuild.description = "\n 构建取消!" 
            }
        }
    }
}
