#!/bin/bash

ROOT_PATH=$(cd $(dirname $0) && pwd)

which java >/dev/null 2>&1
[ $? -ne 0 ] && echo "can't find java" && exit 1

which javac >/dev/null 2>&1
[ $? -ne 0 ] && echo "can't find javac" && exit 1

which adb >/dev/null 2>&1
[ $? -ne 0 ] && echo "can't find adb" && exit 1

javac -encoding utf-8 src/com/company/*.java

cd $ROOT_PATH/src 
java -cp . com.company.BackgroundImage4Panel
