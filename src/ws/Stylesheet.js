stylesheet.nodes.LAL = {
  graphics: {
    label:{gremlin:"label()"}, 
    title:" ",        
    subtitle:" ",     
    group:" ",        
    shape:"image",      
    image:"LAL.png",        
    borderRadius:"0", 
    borderWidth:"1",  
    borderDashes:[1,0],
    value:"0"         
    },
  actions:[
    {name:"Livy",         url:{gremlin:"properties('livy'        ).value()"}},
    {name:"Spark",        url:{gremlin:"properties('spark'       ).value()"}},
    {name:"SparkHistory", url:{gremlin:"properties('sparkHistory').value()"}},
    {name:"Hadoop",       url:{gremlin:"properties('hadoop'      ).value()"}},
    {name:"HBase",        url:{gremlin:"properties('hbase'       ).value()"}},
    {name:"Ganglia",      url:{gremlin:"properties('ganglia'     ).value()"}}
    ]
  }
stylesheet.nodes.Local = {
  graphics: {
    label:{gremlin:"label()"}, 
    title:" ",        
    subtitle:" ",     
    group:" ",        
    shape:"image",      
    image:"Local.png",        
    borderRadius:"0", 
    borderWidth:"1",  
    borderDashes:[1,0],
    value:"0"         
    },
  actions:[
    {name:"Livy",         url:{gremlin:"properties('livy'        ).value()"}},
    {name:"Spark",        url:{gremlin:"properties('spark'       ).value()"}},
    {name:"SparkHistory", url:{gremlin:"properties('sparkHistory').value()"}},
    {name:"Hadoop",       url:{gremlin:"properties('hadoop'      ).value()"}},
    {name:"HBase",        url:{gremlin:"properties('hbase'       ).value()"}},
    {name:"Ganglia",      url:{gremlin:"properties('ganglia'     ).value()"}}
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
    {name:"info", url:"https://astrolabsoftware.github.io"}
    ]
  }
stylesheet.nodes.alert = {
  graphics: {
    label:{gremlin:"values('id')"}, 
    title:{gremlin:"values('id')"},        
    subtitle:{gremlin:"sideEffect(outE().count().store('o')).sideEffect(inE().count().store('i')).cap('o', 'i').next().values().join().toString().replace('][', ' out, ').replace(']', ' in').replace('[', '')"},     
    group:" ",        
    shape:"dot",      
    image:" ",        
    borderRadius:"0", 
    borderWidth:"1",  
    borderDashes:[1,0],
    value: {gremlin:"both().count().join().toString()"}        
    },
  actions:[
    {name:"info", url:"https://astrolabsoftware.github.io"}
    ]
  }
stylesheet.edges.is = {
  graphics: {
    label:" ",
    title:" ",
    subtitle:" ",
    arrows:"to",
    value:"1",
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
    arrows:"to",
    value:"1",
    group:"knows"
    },
  actions: [
    {name:"info", url:"https://astrolabsoftware.github.io"}
    ]
  }
