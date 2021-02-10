package org.devops

//构建
def Build(buildType,buildShell){
    def buildTool = ["mvn":"m3","ant":"ant","gradle":"gradle","npm":"node"]

    buildHome = tool buildTool[buildType]
    
    if ("${buildType}" == "npm"){
        sh  """
            export NODE_HOME=${buildHome}
            export PATH=\${NODE_HOME}/bin:\$PATH
            ${buildHome}/bin/${buildType} ${buildShell}
            """
    }
    else {
        sh "${buildHome}/bin/${buildType} ${buildShell}"
    }
}
