#!/bin/bash

SD=$(cd `dirname $0`; pwd)

APK="$SD/app/build/outputs/apk/app-debug.apk"
REMOTEDEST="/var/www/simpletimer/download/simple-timer.apk"
REMOTEDEST="/var/www/simpletimer/download/test.apk"
REMOTETMP="/tmp/.simpletimer-deploy.tmp"

cat "$APK" | ssh mhm "cat > $REMOTETMP && cp $REMOTETMP $REMOTEDEST ; rm -f $REMOTETMP" 
ret=$?

if [ $ret -eq 0 ]
then
  echo "Deploy success"
else
  echo "Deploy failure" >&2
fi

