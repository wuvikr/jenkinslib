#!groovy
@Library('jenkins_lib') _
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
        string description: '请填写项目仓库地址：', name: 'gitURL', defaultValue: 'http://gitlab.wuvikr.top/devops/demo-java-helloworld.git'
        string description: '请选择项目分支：', name: 'branchName', defaultValue: 'main'
        booleanParam description: '是否使用sonar进行代码扫描？（默认：false）', name: 'sonarScan'
    }


    stages {
        //下载代码
        stage("GetCode"){ //阶段名称
            steps{  //步骤
                timeout(time:5, unit:"MINUTES"){
                    script{
                        tools.PrintMes("获取代码!","green")
                        checkout.GetCode(gitURL,branchName)
                    }
                }
            }
        }
        
        //构建
        stage("Build"){
            steps{
                timeout(time:20, unit:"MINUTES"){
                    script{
                        tools.PrintMes("应用打包!","green")
                        build.CodeBuild("maven")
                    }
                }
            }
        }
        
        //代码扫描
        stage("CodeScan"){
            when { environment name: 'sonarScan', value: 'true' }
            steps{
                timeout(time:20, unit:"MINUTES"){
                    script{
                        tools.PrintMes("代码扫描!","green")
                        sast.SonarScanner()
                    }
                }
            }
        }

        //上传制品
        stage("uploadArtifact"){
            steps{
                script{
                    tools.PrintMes("上传制品!","green")
                    artifact.upload()
                }
                
            }
        }
    }

    //构建后操作
    post {
        always {
            script{
                println("delete Dir")
                //deleteDir()
            }
        }

        success {
            script{
                currentBuild.description = "\n 上传制品成功!" 
            }
        }

        failure {
            script{
                currentBuild.description = "\n 上传制品失败!" 
            }
        }

        aborted {
            script{
                currentBuild.description = "\n 上传制品取消!" 
            }
        }
    }
}
