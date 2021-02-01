#!groovy
@Library('jenkinslib') _

def tools = new org.devops.jenkinslib()

String workspace = "/opt/jenkins/workspace"

//Pipeline
pipeline {
    agent { node {  label "master"   //指定运行节点的标签或者名称
                    customWorkspace "${workspace}"   //指定运行工作目录（可选）
            }
    }

    tools {
        maven 'm3'
    }
    
    options {
        timestamps()  //日志会有时间
        skipDefaultCheckout()  //删除隐式checkout scm语句
        disableConcurrentBuilds() //禁止并行
        timeout(time: 1, unit: 'HOURS')  //流水线超时设置1h
    }

    stages {
        //下载代码
        stage("GetCode"){ //阶段名称
            steps{  //步骤
                timeout(time:5, unit:"MINUTES"){   //步骤超时时间
                    script{ //填写运行代码
                        println('获取代码')
                        input id: 'Test', message: '是否要继续本次构建', ok: '是，我要继续', parameters: [choice(choices: ['a', 'b'], description: '', name: 'test')], submitter: 'admin'
                    }
                }
                sh 'mvn --version'
            }
        }
        
        stage('Example') {
            when { environment name: 'test-str', value: '1234'}

            //局部变量
            environment { 
                a = 'bbccd'
            }
            steps {
                sh 'printenv'
            }
        }
        
        //构建
        stage("Build"){
            steps{
                timeout(time:20, unit:"MINUTES"){
                    script{
                        println('应用打包')
                        mvnHome = tool 'm3'
                        println(mvnHome)
                        
                    }
                }
            }
        }
        
        //代码扫描
        stage("CodeScan"){
            steps{
                timeout(time:30, unit:"MINUTES"){
                    script{
                        print("代码扫描")
                        
                        tools.PrintMes("This is my lib!")
                    }
                }
            }
        }
    }

    //构建后操作
    post {
        always {
            script{
                println("always")
            }
        }

        success {
            script{
                currentBuild.description = "\n 构建成功!" 
            }
        }

        failure {
            script{
                currentBuild.description = "\n 构建失败!" 
            }
        }

        aborted {
            script{
                currentBuild.description = "\n 构建取消!" 
            }
        }
    }
}
