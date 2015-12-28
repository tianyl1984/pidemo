#!/bin/sh
if [ ! -n "$1" ]; then
  echo "未指定执行的类"
  exit
fi
cd /home/pi/pidemo
git pull;
mvn -f /home/pi/pidemo/pom.xml clean package;

sudo java -classpath /opt/pi4j/lib/'*':/home/pi/pidemo/target/webapp/WEB-INF/lib/'*':/home/pi/pidemo/target/webapp/WEB-INF/classes com.tianyl.pidemo.$1

