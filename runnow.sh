#!/bin/sh

port=8050
mvnexe="mvn"
prof=dev
echo "run web app!!!"


while getopts "p:P:t:d" arg
do
        case $arg in
            p)
                port="$OPTARG"
                ;;
            d)
                mvnexe="mvnDebug"
                ;;
            P)
                prof="$OPTARG"
                ;;
            t)
                tpl="file://$OPTARG"
                ;;
            ?)
            echo "unkonw argument"
        exit 1
        ;;
        esac
done
echo $prof
echo $port

mvn clean
$mvnexe -U jetty:run-exploded -Djetty.port=$port -Dprofile=$prof -Dmaven.test.skip=true