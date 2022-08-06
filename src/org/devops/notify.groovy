package org.devops

//构建
def weixin() {
    wrap([$class: 'BuildUser']){
        //echo "full name is $BUILD_USER"
        //echo "user id is $BUILD_USER_ID"
        //echo "user email is $BUILD_USER_EMAIL"
        env.BUILD_USER = "admin"
    }
    sh """
        curl --location --request POST 'https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=eddfgdso-ed3b-4dab-8543-c108df256971' \
        --header 'Content-Type: application/json' \
        --data '{
            "msgtype": "markdown",
            "markdown": {
                "content": "## ${JOB_NAME}作业构建信息: \n  ### 构建人：${env.BUILD_USER} \n   ### 作业状态： ${currentBuild.currentResult} \n  ### 运行时长： ${currentBuild.durationString} \n  ###### 更多详细信息点击 [构建日志](${BUILD_URL}/console) \n"
            }
        }'
    """
}
