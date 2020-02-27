source build-global.properties
source build-local.properties

source build-global.properties
source build-local.properties

if [[ "x" = "x${ANT_HOME}" ]]; then
  export ANT_HOME="${ant}"
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

alias gremlin='CLASSPATH="" ${janusgraph_dir}/bin/gremlin.sh -i "${jhtools_src}/gremlin/start.gremlin" -i ../src/gremlin/start2.gremlin'
alias gremlin_local='CLASSPATH="" ${janusgraph_dir}/bin/gremlin.sh -i ${jhtools_src}/gremlin/start_local.gremlin ${zookeeper} ${hbase_table} ${home} -i ../src/gremlin/start2_local.gremlin'
alias phoenix_cli='${phoenix_cli}'

echo "commands: gremlin, gremlin_local"


