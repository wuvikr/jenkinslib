#!groovy
@Library('sharable-lib-github') _
import org.devops.*

def artifact = new Artifacts()
def tools = new Tools()

properties([parameters([[$class: 'CascadeChoiceParameter', choiceType: 'PT_CHECKBOX', filterLength: 1, filterable: false, name: 'HOSTS', randomName: 'choice-parameter-302444359802368', referencedParameters: 'ENVIRONMENT', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], oldScript: '', sandbox: false, script: ''], script: [classpath: [], oldScript: '', sandbox: false, script: '''if ( ENVIRONMENT == "DEV" ) {
    return [\'10.0.0.203\', \'10.0.0.204\']
}
else if (ENVIRONMENT == "QA") {
    return [\'10.0.1.101\', \'10.0.1.102\']
}
else if ( ENVIRONMENT.equals("PROD")) {
    return [\'10.0.224.201\', \'10.0.224.202\', \'10.0.224.203\']
}''']]]])])


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
        string description: '请填写要部署的artifact名称：(例如：webDemo-e7e25b5-14.jar)', name: 'artifactiName', defaultValue: ''
        choice choices: ['DEV', 'QA', 'PROD'], description: '请选择要部署的环境：', name: 'ENVIRONMENT'
    }

    stages {
        // 定义变量
        stage('Define Variable') {
            steps {
                script {
                    buName = "${env.JOB_BASE_NAME}".split("-")[0]
                    serviceName = "${env.JOB_BASE_NAME}".split("-")[1]
                }  
            }
        }
        
        // 下载制品
        stage("DownloadArtifact"){
            steps{
                script{
                    tools.PrintMes("获取制品!","green")

                    //nexus下载链接示例：http://10.0.0.204:8081/repository/devops-artifact/mytest/webDemo/main/webDemo-e7e25b5-14.jar
                    urlPath = "${buName}/${serviceName}/${params.version}/${params.artifactiName}"

                    artifact.DownloadArtifact(urlPath)
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
