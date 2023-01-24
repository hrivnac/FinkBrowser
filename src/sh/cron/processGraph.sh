#!/usr/bin/bash -l
F=${1}
LOG=/tmp/process.${F}
cd ~/FinkBrowser/ant
source ./setup.sh
gremlin_console_IJCLab < ${F} | ${LOG}.log 2>&1