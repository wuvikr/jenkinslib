#!groovy
@Library('jenkins_lib') _
import org.devops.*

def artifact = new Artifacts()
def tools = new Tools()

//定义全局变量
env.buName = "${env.JOB_BASE_NAME}".split("-")[0]
env.serviceName = "${env.JOB_BASE_NAME}".split("-")[1]

//Pipeline
pipeline {
    agent { label 'build01' }
    
    options {
        timestamps()                        //日志会有时间
        disableConcurrentBuilds()           //禁止并行
        timeout(time: 30, unit: 'MINUTES')  //流水线超时设置30分钟
        ansiColor('xterm')

        // 只保留最近5次构建记录
        buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')
    }

    parameters {
        string description: '请填写发布版本分支：', name: 'version', defaultValue: 'main'
        string description: '请填写要部署的artifact名称：', name: 'artifactiName', defaultValue: ''
    }

    stages {
        // 下载制品
        stage("DownloadArtifact"){
            steps{
                script{
                    tools.PrintMes("获取制品!","green")
                    artifact.DownloadArtifact()
                }
            }
        }

        // 发布制品
        stage("DeployArtifact"){
            steps{
                script{
                    tools.PrintMes("发布制品!","green")
                    //artifact.DeployArtifact()
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
                currentBuild.description = "\n 部署成功!" 
            }
        }

        failure {
            script{
                currentBuild.description = "\n 部署失败!" 
            }
        }

        aborted {
            script{
                currentBuild.description = "\n 部署取消!" 
            }
        }
    }
}
