package org.devops

//Maven
def MavenBuild(){
    sh "mvn clean package -DskipTests -s settings.xml"
}

//Gradle
def GradleBuild(){
   sh "gradle build -x test"
}

//Ant
def AntBuild(configPath="./build.xml"){
    sh "ant -f ${configPath}"
}

//Golang
def GoBuild(){
    sh " go build demo.go"
}

//Npm
def NpmBuild(){
    sh "npm install && npm run build"
}

//Yarn
def YarnBuild(){
    sh "yarn install && yarn build "
}

//Main
def CodeBuild(type){
    switch(type){
        case "maven":
            MavenBuild()
            break;
        case "gradle":
            GradleBuild()
            break;
        case "npm":
            NpmBuild()
            break;
        case "yarn":
            YarnBuild()
            break;
        default:
            error "No such tools ... [maven/ant/gradle/npm/yarn/go]"
            break
    }
}
