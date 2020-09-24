stylesheet.nodes.AstroLabNet = {
  graphics: {
    label:"AstroLabNet", 
    title:"AstroLabNet",        
    subtitle:" ",     
    group:" ",        
    shape:"image",      
    image:"AstroLab.png",        
    borderRadius:"0", 
    borderWidth:"1",  
    borderDashes:[1,0],
    value:"0"         
    },
  actions:[
    ]
  }
stylesheet.nodes.site = {
  graphics: {
    label:"title", 
    title:"title",        
    subtitle:" ",     
    group:" ",        
    shape:"image",      
    image:{js:"title.replace('site:', '') + '.png'"},
    borderRadius:"0", 
    borderWidth:"1",  
    borderDashes:[1,0],
    value:"0"         
    },
  actions:[
    {name:"Livy",               url:{gremlin:"properties('Livy'          ).value()"}},
    {name:"Spark",              url:{gremlin:"properties('Spark'         ).value()"}},
    {name:"Spark History",      url:{gremlin:"properties('Spark History' ).value()"}},
    {name:"Ganglia",            url:{gremlin:"properties('Ganglia'       ).value()"}},
    {name:"Hadoop",             url:{gremlin:"properties('Hadoop'        ).value()"}},
    {name:"HBase",              url:{gremlin:"properties('HBase'         ).value()"}},
    {name:"Prometheus",         url:{gremlin:"properties('Prometheus'    ).value()"}},
    {name:"Mesos",              url:{gremlin:"properties('Mesos'         ).value()"}},
    {name:"Grafana",            url:{gremlin:"properties('Grafana'       ).value()"}},
    {name:"Zeppelin",           url:{gremlin:"properties('Zeppelin'      ).value()"}},
    {name:"Tomcat",             url:{gremlin:"properties('Tomcat'        ).value()"}},
    {name:"HBase test",         url:{gremlin:"properties('HBaseTest'     ).value()"}, embedded:true},
    {name:"HBase test old",     url:{gremlin:"properties('HBaseTestOld'  ).value()"}, embedded:true},
    {name:"HBase test tiny",    url:{gremlin:"properties('HBaseTestTiny' ).value()"}, embedded:true},
    {name:"HBase test tiny 1",  url:{gremlin:"properties('HBaseTestTiny1').value()"}, embedded:true},
    {name:"HBase test tiny 2",  url:{gremlin:"properties('HBaseTestTiny2').value()"}, embedded:true},
    {name:"HBase test tiny 3",  url:{gremlin:"properties('HBaseTestTiny3').value()"}, embedded:true}
    ]
  }
stylesheet.nodes.alert = {
  graphics: {
    label:"rowkey",
    title:"rowkey",        
    subtitle:{gremlin:"sideEffect(values('classtar').store('1')).sideEffect(values('rfscore').store('2')).sideEffect(values('snnscore').store('3')).sideEffect(values('cdxmatch').store('4')).sideEffect(values('mulens_class_1').store('5')).sideEffect(values('mulens_class_2').store('6')).sideEffect(values('roid').store('7')).cap('1', '2', '3', '4', '5', '6', '7').next().values().join().replaceFirst('\\\\[', '<br>classtar=').replaceFirst('\\\\[', 'rfscore=').replaceFirst('\\\\[', 'snscore=').replaceFirst('\\\\[', 'dxmatch=').replaceFirst('\\\\[', 'mulens_class_1=').replaceFirst('\\\\[', 'mulens_class_2=').replaceFirst('\\\\[', 'roid=').replace(']', '<br>')"},
    group:" ",        
    shape:"image",      
    image:"Alert.png",        
    borderRadius:"0", 
    borderWidth:"1",  
    borderDashes:[1,0],
    value: {gremlin:"both().count().join().toString()"}        
    },
  actions:[
    ]
  }
stylesheet.nodes.similarity = {
  graphics: {
    label:"recipe",
    title:"recipe",        
    subtitle:{gremlin:"sideEffect(values('recipe').store('1')).sideEffect(values('equals').store('2')).cap('1', '2').next().values().join().replaceFirst('\\\\[', '<br>recipe=').replaceFirst('\\\\[', 'equals=').replace(']', '<br>')"},
    group:" ",        
    shape:"image",      
    image:"Similarity.png",        
    borderRadius:"0", 
    borderWidth:"1",  
    borderDashes:[1,0],
    value: {gremlin:"both().count().join().toString()"}        
    },
  actions:[
    ]
  }
stylesheet.nodes.AlertsCollection = {
  graphics: {
    label:"title", 
    title:"title",        
    subtitle:" ",     
    group:" ",        
    shape:"image",      
    image:"Alerts.png",        
    borderRadius:"0", 
    borderWidth:"1",  
    borderDashes:[1,0],
    value: {gremlin:"both().count().join().toString()"}        
    },
  actions:[
    {name:"Latest Alerts",  url:{gremlin:"properties('hbase').value()"}, embedded:true}
    ]
  }
stylesheet.edges.has = {
  graphics: {
    label:" ",
    title:" ",
    subtitle:" ",
    arrows:{to:{enabled:true, type:"vee"}},
    value: "0.1",
    group: " "
    },
  actions: [
    ]
  }
stylesheet.edges.holds = {
  graphics: {
    label:" ",
    title:" ",
    subtitle:" ",
    arrows:{to:{enabled:true, type:"vee"}},
    value: "0.1",
    group: " "
    },
  actions: [
    ]
  }
stylesheet.edges.contains = {
  graphics: {
    label:" ",
    title:" ",
    subtitle:" ",
    arrows:{to:{enabled:true, type:"vee"}},
    value: "0.1",
    group: " "
    },
  actions: [
    ]
  }
stylesheet.edges.satr = {
  graphics: {
    label:{gremlin:"properties('weight').next().value().toString()"},
    title:{gremlin:"properties('weight').next().value().toString()"},
    subtitle:" ",
    arrows:{to:{enabled:true, type:"vee"}},
    value:"0.1",
    group:{gremlin:"properties('weight').next().value().toString()"}
    },
  actions: [
    ]
  }
stylesheet.edges.exactmatch = {
  graphics: {
    label:{gremlin:"properties('propertyname').next().value().toString()"},
    title:{gremlin:"properties('propertyname').next().value().toString()"},
    subtitle:" ",
    arrows:{to:{enabled:false, type:"vee"}},
    value: "0.1",
    group: " "
    },
  actions: [
    ]
  }
