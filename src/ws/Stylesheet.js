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
    {name:"Livy",               url:{gremlin:"properties('livy'         ).value()"}},
    {name:"Spark",              url:{gremlin:"properties('spark'        ).value()"}},
    {name:"SparkHistory",       url:{gremlin:"properties('sparkHistory' ).value()"}},
    {name:"Hadoop",             url:{gremlin:"properties('hadoop'       ).value()"}},
    {name:"HBase",              url:{gremlin:"properties('hbase'        ).value()"}},
    {name:"Ganglia",            url:{gremlin:"properties('ganglia'      ).value()"}},
    {name:"HBase test",         url:{gremlin:"properties('hbaseTest'    ).value()"}, embedded:true},
    {name:"HBase test old",     url:{gremlin:"properties('hbaseTestOld' ).value()"}, embedded:true},
    {name:"HBase test tiny 1",  url:{gremlin:"properties('hbaseTestTiny1').value()"}, embedded:true},
    {name:"HBase test tiny 2",  url:{gremlin:"properties('hbaseTestTiny2').value()"}, embedded:true}
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
stylesheet.nodes.vertex = {
  graphics: {
    label:"objectId", 
    title:"objectId",        
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
    ]
  }
stylesheet.nodes.Alert = {
  graphics: {
    label:"title", 
    title:"title",        
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
stylesheet.edges.similarity = {
  graphics: {
    label:"similarity",
    title:"value",
    subtitle:" ",
    arrows:{middle:{enabled:false, type:"arrow"}},
    value:"value",
    group:"similarity"
    },
  actions: [
    {name:"info", url:"https://astrolabsoftware.github.io"}
    ]
  }
