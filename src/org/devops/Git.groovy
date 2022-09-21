package org.devops

// checkout
def Checkout(srcUrl, branchName){
    checkout([$class: 'GitSCM', 
                branches: [[name: branchName]], 
                extensions: [], 
                userRemoteConfigs: [[
                credentialsId: '60a21813-9a21-4ee5-a2b0-1fa72cf964d2', 
                url: srcUrl]]])
}

// git
def GetCode(srcUrl, branchName){
    git branch: branchName, credentialsId: '60a21813-9a21-4ee5-a2b0-1fa72cf964d2', url: srcUrl
}

// 获取CommitID
def GetCommitID(){
    CommitID = sh returnStdout: true, script: "git rev-parse HEAD"
    return CommitID[0..7]
}
