package org.devops

 //下载代码
 def GetCode(srcUrl, branchName){
    checkout([$class: 'GitSCM', 
            branches: [[name: branchName]], 
            extensions: [], 
            userRemoteConfigs: [[
            credentialsId: 'gitlab-wvuikr', 
            url: srcUrl]]])
}
