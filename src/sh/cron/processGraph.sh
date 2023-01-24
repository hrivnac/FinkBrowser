#!/usr/bin/bash -l
F=${1}
LOG=/tmp/process.${F}
cd ~/FinkBrowser/ant
source ./setup.sh
CLASSPATH="${GREMLIN_CLASSPATH}" ${janusgraph_dir}/bin/gremlin.sh -i "${lomikel_src}/gremlin/start_console.gremlin ${janusgraph_dir}/conf/gremlin-server/Local-IJCLab.properties ${home}" -i "../src/gremlin/start2_console.gremlin" < ${F} | ${LOG}.log 2>&1