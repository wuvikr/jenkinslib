package org.devops

def Ansible(){
    //将主机写入清单文件
    sh "rm -fr hosts"
    for (host in "${env.deployHosts}".split(',')){
        sh " echo ${host} >> hosts"
    }

    // ansible 发布jar
    sh """
        # 主机连通性检测
        ansible "${env.deployHosts}" -m ping -i hosts
        # 清理和创建发布目录
        ansible "${env.deployHosts}" -m shell -a "rm -fr ${env.targetDir}/${env.projectName}/* &&  mkdir -p ${env.targetDir}/${env.projectName} || echo file is exists"
        # 复制app
        ansible "${env.deployHosts}" -m copy -a "src=${env.pkgName}  dest=${env.targetDir}/${env.projectName}/${env.pkgName}"
    """
    // 发布脚本
    fileData = libraryResource 'scripts/service.sh'
    //println(fileData)
    writeFile file: 'service.sh', text: fileData
    //sh "ls -a ; cat service.sh "

    sh """
        # 复制脚本
        ansible "${env.deployHosts}" -m copy -a "src=service.sh  dest=${env.targetDir}/${env.projectName}/service.sh"

        # 启动服务
        ansible "${env.deployHosts}" -m shell -a "cd ${env.targetDir}/${env.projectName} ;source /etc/profile  && sh service.sh ${env.projectName} ${env.releaseVersion} ${env.port} start" -u root

        # 检查服务
        sleep 10
        ansible "${env.deployHosts}" -m shell -a "cd ${env.targetDir}/${env.projectName} ;source /etc/profile  && sh service.sh ${env.projectName} ${env.releaseVersion} ${env.port} check" -u root
    """
}
