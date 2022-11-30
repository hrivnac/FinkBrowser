#!/usr/bin/bash -l
<<<<<<< HEAD
Y=${1}
M=${2}
D=${3}
LOG=/tmp/upload.${Y}.${M}.${D}
=======
M=${1}
D=${2}
LOG=/tmp/upload2021${M}${D}
/bin/rm -f ${LOG}
>>>>>>> 2b6dab380f100a97090eda324089838ea03bcf12
cd ~/FinkBrowser/ant
source ./setup.sh
for X in 7; do
  /bin/rm -f ${LOG}${X}.log
<<<<<<< HEAD
  ../src/sh/importAvroHDFS-IJCLab.sh /user/julien.peloton/archive_avro/science/year=${Y}/month=${M}/day=${D}${X} | tee ${LOG}${X}.log 2>&1
=======
  ../src/sh/importAvroHDFS-IJCLab.sh /user/julien.peloton/archive_avro/science/year=2021/month=${M}/day=${D}${X} > ${LOG}${X}.log 2>&1
>>>>>>> 2b6dab380f100a97090eda324089838ea03bcf12
  done
