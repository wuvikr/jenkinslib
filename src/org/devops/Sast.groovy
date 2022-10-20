package org.devops

def SonarScanner(){
    withCredentials([usernamePassword(credentialsId: 'ccbd45a9-e869-4edb-ba0e-10b8266d8e65', passwordVariable: 'PASSWD', usernameVariable: 'USERNAME')]) {
        sh '''
            sonar-scanner \
            -Dsonar.login=${USERNAME} \
            -Dsonar.password=${PASSWD} \
        '''  
    }
}
