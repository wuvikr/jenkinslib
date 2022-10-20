#!/bin/bash

JAR_NAME=$2

function flag_pid(){
    PID=`pgrep -f ${JAR_NAME}.jar`
}

function startup(){
    flag_pid
    if [ ! -z "$PID" ];then
        echo "${JAR_NAME}.jar alread started ."
    else
        nohup /usr/local/jdk/bin/java -Xms512m -Xmx512m -Dapp.home=/home/posx -jar /home/posx/app/${JAR_NAME}.jar --spring.profiles.active=dev >>/home/posx/logs/${JAR_NAME}.log 2>&1 &
        sleep 5
        flag_pid
        if [ -z $PID ];then
            echo "${JAR_NAME}.jar start Failed ."
            exit 5
        else
            echo "${JAR_NAME}.jar start Successed ."
        fi
    fi
}

function shutdown(){
    flag_pid
    if [ -z "$PID" ];then
        echo "${JAR_NAME}.jar has been stopped ."
        return 3
    else
        echo  "${JAR_NAME}.jar is stopping ."
        kill $PID
        sleep 5
        flag_pid
        if [ ! -z "$PID" ];then
            kill -9 $PID
        fi
    fi
    echo "${JAR_NAME}.jar stop successful ."
}

function restart(){
    shutdown
    startup
}

case $1 in
start)
    startup
    ;;
stop)
    shutdown
    ;;
restart)
    restart
    ;;
*)
    echo "Usage: $(basename $0) start|stop|restart JAR_NAME"
    exit 1
    ;;
esac

