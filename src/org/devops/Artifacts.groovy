package org.devops

def UploadSnapshots(){
    script {
        // 读取pom.xml文件内容
        def pomData = readMavenPom file: 'pom.xml'
        def jarName = "${pomData.artifactId}-${pomData.version}"
        
        sh """
            mvn deploy:deploy-file \
                -Dfile=target/${jarName}.jar \
                -Durl=http://10.0.0.204:8081/repository/maven-snapshots \
                -DrepositoryId=my-nexus-maven \
                -DpomFile=pom.xml \
                -DgeneratePom=false
        """
    }
}

def UploadArtifact() {
    // filePath = buName/serviceName/version/
    // fileName = serviceName-commitID-buildNumber.jar

    filePath = "${env.buName}/${env.serviceName}/${params.branchName}"
    println(filePath)

    jarName = sh returnStdout: true, script: """ls target | grep -E "jar\$" """
    localJarPath = "target/${jarName}"

    fileName = "${env.serviceName}-${env.commitID}-${env.BUILD_NUMBER}.jar"

    withCredentials([usernamePassword(credentialsId: '475cc063-5ed1-422b-9436-03873fc06439', passwordVariable: 'PASSWD', usernameVariable: 'USERNAME')]) {
    sh """
        curl -u ${USERNAME}:${PASSWD} -X POST \
            "http://10.0.0.204:8081/service/rest/v1/components?repository=devops-artifact" \
            -H "accept: application/json" \
            -H "Content-Type: multipart/form-data" \
            -F "raw.directory=/${filePath}" \
            -F "raw.asset1=@${localJarPath};type=application/java-archive" \
            -F "raw.asset1.filename=${fileName}"
    """
    }
    
}


def DownloadArtifact() {
    repositoryUrl = 'http://10.0.0.204:8081/repository/devops-artifact/'

    urlPath = "${buName}/${serviceName}/${params.version}/${params.artifactiName}"

    requestUrl = repositoryUrl + urlPath

    withCredentials([usernamePassword(credentialsId: '475cc063-5ed1-422b-9436-03873fc06439', passwordVariable: 'PASSWD', usernameVariable: 'USERNAME')]) {
        sh "curl -u ${USERNAME}:${PASSWD} -O ${requestUrl}"
    }
} 
