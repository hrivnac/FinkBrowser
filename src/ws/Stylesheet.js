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
    {name:"Fink Data Explorer Home",  url:"https://cern.ch/hrivnac/Activities/Packages/FinkBrowser", external:true}
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
    {name:"Livy",               url:{gremlin:"properties('Livy'             )"}, external:true},
    {name:"Spark",              url:{gremlin:"properties('Spark'            )"}, external:true},
    {name:"Spark History",      url:{gremlin:"properties('Spark History'    )"}, external:true},
    {name:"Ganglia",            url:{gremlin:"properties('Ganglia'          )"}, external:true},
    {name:"Hadoop",             url:{gremlin:"properties('Hadoop'           )"}, external:true},
    {name:"HBase",              url:{gremlin:"properties('HBase'            )"}, external:true},
    {name:"Prometheus",         url:{gremlin:"properties('Prometheus'       )"}, external:true},
    {name:"Mesos",              url:{gremlin:"properties('Mesos'            )"}, external:true},
    {name:"Grafana",            url:{gremlin:"properties('Grafana'          )"}, external:true},
    {name:"Zeppelin",           url:{gremlin:"properties('Zeppelin'         )"}, external:true},
    {name:"Tomcat",             url:{gremlin:"properties('Tomcat'           )"}, external:true},
    {name:"HBase ZTF-Season1",  url:{gremlin:"properties('HBase-ZTF-Season1')"}, embedded:true},
    {name:"HBase Test Tiny 3",  url:{gremlin:"properties('HBase-Test-Tiny-3')"}, embedded:true}
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
    {name:"alert",   url:{gremlin:"properties('rowkey').value().next().replaceAll('^',               '/FinkBrowser/HBaseTable.jsp?selects=all&key=')"                                       }, embedded:true},
    {name:"alerts",  url:{gremlin:"properties('rowkey').value().next().split('_')[0].replaceAll('^', '/FinkBrowser/HBaseTable.jsp?selects=all&filters=key:key:').replaceAll('$', ':prefix')"}, embedded:true},
    {name:"Analyse", url:{gremlin:"properties('rowkey').value().next().split('_')[0].replaceAll('^', 'http://134.158.75.151:24000/')"                                           }, external:true}
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
    {name:"Alerts",  url:{gremlin:"properties('hbase')"}, embedded:true}
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
    label:{gremlin:"properties('weight')"},
    title:{gremlin:"properties('weight')"},
    subtitle:" ",
    arrows:{to:{enabled:true, type:"vee"}},
    value:"0.1",
    group:{gremlin:"properties('weight')"}
    },
  actions: [
    ]
  }
stylesheet.edges.exactmatch = {
  graphics: {
    label:{gremlin:"properties('propertyname')"},
    title:{gremlin:"properties('propertyname')"},
    subtitle:" ",
    arrows:{to:{enabled:false, type:"vee"}},
    value: "0.1",
    group: " "
    },
  actions: [
    ]
  }
