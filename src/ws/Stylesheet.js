stylesheet.nodes.site = {
  graphics: {
    label:{gremlin:"properties('title').value()"}, 
    title:{gremlin:"properties('title').value()"},        
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
    {name:"Livy",             url:{gremlin:"properties('livy'         ).value()"}},
    {name:"Spark",            url:{gremlin:"properties('spark'        ).value()"}},
    {name:"SparkHistory",     url:{gremlin:"properties('sparkHistory' ).value()"}},
    {name:"Hadoop",           url:{gremlin:"properties('hadoop'       ).value()"}},
    {name:"HBase",            url:{gremlin:"properties('hbase'        ).value()"}},
    {name:"Ganglia",          url:{gremlin:"properties('ganglia'      ).value()"}},
    {name:"HBase test",       url:{gremlin:"properties('hbaseTest'    ).value()"}, embedded:true},
    {name:"HBase test old",   url:{gremlin:"properties('hbaseTestOld' ).value()"}, embedded:true},
    {name:"HBase test tiny",  url:{gremlin:"properties('hbaseTestTiny').value()"}, embedded:true}
    ]
  }
stylesheet.nodes.AstroLabNet = {
  graphics: {
    label:{gremlin:"label()"}, 
    title:" ",        
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
stylesheet.nodes.Alert = {
  graphics: {
    label:{gremlin:"values('title')"}, 
    title:{gremlin:"values('title')"},        
    subtitle:" ",
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
stylesheet.nodes.AlertsCollection = {
  graphics: {
    label:{gremlin:"values('title')"}, 
    title:{gremlin:"values('title')"},        
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
stylesheet.edges.is = {
  graphics: {
    label:" ",
    title:" ",
    subtitle:" ",
    arrows:{to:{enabled:true, type:"vee"}},
    value:"0.1",
    group:"is"
    },
  actions: [
    {name:"info", url:"https://astrolabsoftware.github.io"}
    ]
  }
stylesheet.edges.knows = {
  graphics: {
    label:" ",
    title:" ",
    subtitle:" ",
    arrows:{middle:{enabled:true, type:"arrow"}},
    value:"0.1",
    group:"knows"
    },
  actions: [
    {name:"info", url:"https://astrolabsoftware.github.io"}
    ]
  }
