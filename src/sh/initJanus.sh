cd ../ant
source setup.sh
curl -X DELETE 'http://127.0.0.1:20200/*'
gremlin_console_Local < ${lomikel_dist}/src-${lomikel_ver}/gremlin/clean.gremlin
gremlin_console_Local < ${lomikel_dist}/src-${lomikel_ver}/gremlin/schema.gremlin
gremlin_console_Local < ${lomikel_dist}/src-${lomikel_ver}/gremlin/indexHB.gremlin
gremlin_console_Local < ${lomikel_dist}/src-${lomikel_ver}/gremlin/indexES.gremlin
gremlin_console_Local < ../src/gremlin/schema.gremlin
gremlin_console_Local < ../src/gremlin/indexHB.gremlin
gremlin_console_Local < ../src/gremlin/indexES.gremlin
gremlin_console_Local < ../src/gremlin/astrolabnet-${target}.gremlin
