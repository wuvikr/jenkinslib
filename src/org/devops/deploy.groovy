package org.devops

//Ansible
def AnsibleDeploy(hosts,func){
    sh "ansible ${hosts} ${func}"
}
