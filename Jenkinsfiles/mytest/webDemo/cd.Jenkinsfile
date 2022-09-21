#!groovy
@Library('jenkins_lib') _
import org.devops.*

def artifact = new Artifacts()
def tools = new Tools()

//定义全局变量
env.buName = "${env.JOB_BASE_NAME}".split("-")[0]
env.serviceName = "${env.JOB_BASE_NAME}".split("-")[1]

properties([parameters([
             [$class: 'CascadeChoiceParameter',
               choiceType: 'PT_SINGLE_SELECT',
               description: '请选择需要部署的主机：',
               filterLength: 1,
               filterable: false,
               name: 'CLUSTER',
               referencedParameters: 'ENVIRONMENT',
               script: [$class: 'GroovyScript',
                         fallbackScript:[classpath: [], sandbox: false, script: ''],
                         script: [classpath: [], sandbox: false,
                                  script: '''if ( ENVIRONMENT == "DEV" ) {
                                                 return [\'10.0.0.203\', \'10.0.0.204\']
                                             }
                                             else if (ENVIRONMENT == "QA") {
                                                 return [\'10.0.1.101\', \'10.0.1.102\']
                                             }
                                             else if ( ENVIRONMENT.equals("PROD")) {
                                                 return [\'10.0.224.201\', \'10.0.224.202\', \'10.0.224.203\']
                                             }'''
                                 ]
                         ]
             ]
            ])
])

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
        choice choices: ['DEV', 'QA', 'PROD'], description: '请选择要部署的环境：', name: 'ENVIRONMENT'
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
