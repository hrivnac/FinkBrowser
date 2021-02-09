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

export janusgraph_dir
export zookeeper
export hbase_table

export home=`pwd`/..

alias gremlin_Local='CLASSPATH=""   ${janusgraph_dir}/bin/gremlin.sh -i "${lomikel_src}/gremlin/start_Local.gremlin"                                       -i "../src/gremlin/start2.gremlin"'
alias gremlin_IJCLab='CLASSPATH=""  ${janusgraph_dir}/bin/gremlin.sh -i "${lomikel_src}/gremlin/start_IJCLab.gremlin"                                      -i "../src/gremlin/start2.gremlin"'
alias gremlin_console='CLASSPATH="" ${janusgraph_dir}/bin/gremlin.sh -i "${lomikel_src}/gremlin/start_console.gremlin ${zookeeper} ${janus_table} ${hbase_table} ${hbase_port} ${hbase_schema} ${rowkey_name} ${home}"'

echo "commands: gremlin_console, gremlin_Local, gremlin_IJCLab"


