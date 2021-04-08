stylesheet.nodes.AstroLabNet = {
  properties:{},
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
  properties:{gremlin:"valueMap('lbl', 'Livy', 'Spark', 'Spark History', 'Ganglia', 'Hadoop', 'HBase', 'Prometheus', 'Mesos', 'Grafana', 'Zeppelin', 'Tomcat', 'HBase_ZTF_Season1', 'HBase_Test_Tiny_3')"},
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
    {name:"Livy",               url:"Livy"             , external:true},
    {name:"Spark",              url:"Spark"            , external:true},
    {name:"Spark History",      url:"Spark History"    , external:true},
    {name:"Ganglia",            url:"Ganglia"          , external:true},
    {name:"Hadoop",             url:"Hadoop"           , external:true},
    {name:"HBase",              url:"HBase"            , external:true},
    {name:"Prometheus",         url:"Prometheus"       , external:true},
    {name:"Mesos",              url:"Mesos"            , external:true},
    {name:"Grafana",            url:"Grafana"          , external:true},
    {name:"Zeppelin",           url:"Zeppelin"         , external:true},
    {name:"Tomcat",             url:"Tomcat"           , external:true},
    {name:"HBase ZTF-Season1",  url:"HBase_ZTF_Season1", embedded:true},
    {name:"HBase Test Tiny 3",  url:"HBase_Test_Tiny_3", embedded:true}
    ]
  }
stylesheet.nodes.alert = {
  properties:{gremlin:"valueMap('rowkey')"},
  graphics: {
    label:{js:"rowkey"},
    title:{js:"rowkey"},        
    subtitle:" ",
    group:" ",        
    shape:"image",      
    image:"Alert.png",        
    borderRadius:"0", 
    borderWidth:"1",  
    borderDashes:[1,0],
    value: {gremlin:"both().count().join().toString()"}        
    },
  actions:[
    {name:"alert",   url:{js:"'/FinkBrowser/HBaseTable.jsp?selects=all&key=' + rowkey"                                     }, embedded:true},
    {name:"alerts",  url:{js:"'/FinkBrowser/HBaseTable.jsp?selects=all&filters=key:key:' + rowkey.split('_')[0]+ ':prefix'"}, embedded:true},
    {name:"Analyse", url:{js:"'http://134.158.75.151:24000/' + rowkey.split('_')[0]"                                       }, external:true}
    ]
  }
stylesheet.nodes.AlertsCollection = {
  properties:{gremlin:"valueMap('hbase')"},
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
    {name:"Alerts", url:{js:"hbase"}, embedded:true}
    ]
  }
stylesheet.edges.has = {
  properties:{},
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
  properties:{},
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
  properties:{},
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
