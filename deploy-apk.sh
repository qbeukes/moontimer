#!/bin/bash

SD=$(cd `dirname $0`; pwd)

APK="$SD/app/app-release.apk"
REMOTEDEST="/var/www/moontimer/download/moontimer.apk"
REMOTETMP="/tmp/.moontimer-deploy.tmp"

cat "$APK" | ssh mhm "cat > $REMOTETMP && cp $REMOTETMP $REMOTEDEST && rm -f $REMOTETMP" 
ret=$?

if [ $ret -eq 0 ]
then
  echo "Deploy success"
else
  echo "Deploy failure" >&2
fi

