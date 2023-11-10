source build-global.properties
source build-local.properties

if [[ ! "x" = "x${ant_home}" ]]; then
  export ANT_HOME="${ant_home}"
  export PATH="${ANT_HOME}/bin:${PATH}"
  fi
if [[ ! "x" = "x${graphviz}" ]]; then
  export PATH="${graphviz}/bin:${PATH}"
  fi
for JAR in ../extlib/*.jar; do
  export CLASSPATH=${CLASSPATH}:${JAR} 
  done
export GREMLIN_CLASSPATH="${groovy_sql_jar}:${bsh_jar}:${healpix_jar}"  

export janusgraph_dir
export zookeeper
export hbase_table

export home=`pwd`/..

alias gremlin_console_Local='CLASSPATH="${GREMLIN_CLASSPATH}"  ${janusgraph_dir}/bin/gremlin.sh -i "${lomikel_src}/gremlin/start_console.gremlin ${janusgraph_dir}/conf/gremlin-server/Local.properties ${home}"        -i "../src/gremlin/start2_console.gremlin"'
alias gremlin_console_IJCLab='CLASSPATH="${GREMLIN_CLASSPATH}" ${janusgraph_dir}/bin/gremlin.sh -i "${lomikel_src}/gremlin/start_console.gremlin ${janusgraph_dir}/conf/gremlin-server/Local-IJCLab.properties ${home}" -i "../src/gremlin/start2_console.gremlin"'
alias gremlin_Local='CLASSPATH=""  ${janusgraph_dir}/bin/gremlin.sh -i "${lomikel_src}/gremlin/start_Local.gremlin"  -i "../src/gremlin/start2.gremlin"'
alias gremlin_IJCLab='CLASSPATH="" ${janusgraph_dir}/bin/gremlin.sh -i "${lomikel_src}/gremlin/start_IJCLab.gremlin" -i "../src/gremlin/start2.gremlin"'
alias fink='CLASSPATH="../lib/FinkBrowser-${version}.jar:${lomikel_jar}:${phoenix_jar}" java com.astrolabsoftware.FinkBrowser.Apps.FUC'

echo "commands: gremlin_console_Local, gremlin_console_IJCLab, gremlin_Local, gremlin_IJCLab, fink"


