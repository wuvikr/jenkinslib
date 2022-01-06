package org.devops

 //下载代码
 def GetCode(srcUrl, branchName){
    checkout([$class: 'GitSCM', 
            branches: [[name: branchName]], 
            extensions: [], 
            userRemoteConfigs: [[
            credentialsId: '7eaa6c7f-f954-4a65-bcd7-15f27edb680b', 
            url: srcUrl]]])
}
