#!/bin/bash

SD=$(cd `dirname $0`; pwd)

APK="$SD/app/build/outputs/apk/app-debug.apk"
REMOTEDEST="/var/www/simpletimer/download/simple-timer.apk"

cat "$APK" | ssh mhm "cat > /tmp/.simpletimer-deploy.tmp && cp /tmp/.simpletimer-deploy.tmp $REMOTEDEST" 
ret=$?

if [ $ret -eq 0 ]
then
  echo "Deploy success"
else
  echo "Deploy failure" >&2
fi

