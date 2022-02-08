#!/usr/bin/bash -l
D=${1}
LOG=/tmp/upload202109${D}
cd ~/FinkBrowser/ant
source ./setup.sh
for X in 0 1 2 3 4 5 6 7 8 9; do
  /bin/rm -f ${LOG}${X}.log
  ../src/sh/importAvroHDFS-IJCLab.sh /user/julien.peloton/archive_avro/science/year=2021/month=09/day=${D}${X} > ${LOG}${X}.log 2>&1
  done
