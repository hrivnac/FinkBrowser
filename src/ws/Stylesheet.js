// TBD: allow properties of non-string values

function storeData(fn, dataS) {
  return "FITSView.jsp?fn=" + fn + "&data=" + encodeURIComponent(dataS);
  }

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
    {name:"Fink Data Explorer Home",  url:"https://cern.ch/hrivnac/Activities/Packages/FinkBrowser",                target:"external"},
    {name:"Show",                     url:{gremlin:"id().next().toString().replaceFirst(\"^\", \"Node.jsp?id=\")"}, target:"result"  }
    ]
  }
stylesheet.nodes.site = {
  properties:{gremlin:"valueMap('lbl', 'Livy', 'Spark', 'Spark History', 'Ganglia', 'Hadoop', 'HBase', 'Prometheus', 'Mesos', 'Grafana', 'Zeppelin', 'Tomcat', 'HBase_ZTF_Season1', 'HBase_Test_Tiny_3').toList()[0]"},
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
    {name:"Livy",               url:"Livy"         , target:"external"},
    {name:"Spark",              url:"Spark"        , target:"external"},
    {name:"Spark History",      url:"Spark History", target:"external"},
    {name:"Ganglia",            url:"Ganglia"      , target:"external"},
    {name:"Hadoop",             url:"Hadoop"       , target:"external"},
    {name:"HBase",              url:"HBase"        , target:"external"},
    {name:"Prometheus",         url:"Prometheus"   , target:"external"},
    {name:"Mesos",              url:"Mesos"        , target:"external"},
    {name:"Grafana",            url:"Grafana"      , target:"external"},
    {name:"Zeppelin",           url:"Zeppelin"     , target:"external"},
    {name:"Tomcat",             url:"Tomcat"       , target:"external"}
    ]
  }
stylesheet.nodes.alert = {
  properties:{gremlin:"valueMap('objectId').toList()[0]"},
  graphics: {
    label:"objectId",
    title:"objectId",        
    subtitle:" ",
    group:"objectId",        
    shape:"hexagon",      
    image:"",        
    borderRadius:"0", 
    borderWidth:"3",  
    borderDashes:[1,0],
    value:{gremlin:"out().out().count().join().toString()"}        
    },
  actions:[                                                                 
    {name:"Analyse", url:{js:"'http://134.158.75.151:24000/' + objectId"                        }, target:"external"},
    {name:"Show",    url:{gremlin:"id().next().toString().replaceFirst(\"^\", \"Node.jsp?id=\")"}, target:"result"  }
    ]
  }
stylesheet.nodes.candidate = {
  properties:{gremlin:"valueMap('candid', 'ra', 'dec').toList()[0]"},
  graphics: {
    label:"candid",
    title:"candid",        
    subtitle:" ",
    group:{gremlin:"in().has('lbl', 'alert').values('objectId').toList()[0]"},        
    shape:"dot",      
    image:"",        
    borderRadius:"0", 
    borderWidth:"3",  
    borderDashes:[1,0],
    value:"0"        
    },
  actions:[
    {name:"Sky View", url:{js:"'d3/skyview.jsp?ra=' + ra + '&dec=' + dec"},                         target:"skyview"},
    {name:"Show",     url:{gremlin:"id().next().toString().replaceFirst(\"^\", \"Node.jsp?id=\")"}, target:"result" }
    ]
  }
stylesheet.nodes.prv_candidates = {
  properties:{},
  graphics: {
    label:"prv_candidates",
    title:"prv_candidates",        
    subtitle:" ",
    group:{gremlin:"in().has('lbl','alert').values('objectId').toList()[0]"},        
    shape:"dot",      
    image:"",        
    borderRadius:"0", 
    borderWidth:"3",  
    borderDashes:[1,1],
    value:{gremlin:"out().count().join().toString()"}        
    },
  actions:[
    {name:"Show", url:{gremlin:"id().next().toString().replaceFirst(\"^\", \"Node.jsp?id=\")"}, target:"result"}
    ]
  }
stylesheet.nodes.prv_candidate = {
  properties:{gremlin:"valueMap('jd').toList()[0]"},
  graphics: {
    label:"jd",
    title:"jd",        
    subtitle:" ",
    group:{gremlin:"in().in().has('lbl','alert').values('objectId').toList()[0]"},        
    shape:"dot",      
    image:"",        
    borderRadius:"0", 
    borderWidth:"1",  
    borderDashes:[1,1],
    value:"0"        
    },
  actions:[
    {name:"Show", url:{gremlin:"id().next().toString().replaceFirst(\"^\", \"Node.jsp?id=\")"}, target:"result"}
    ]
  }
stylesheet.nodes.mulens = {
  properties:{gremlin:"valueMap('mulens').toList()[0]"},
  graphics: {
    label:"mulens",
    title:"mulens",        
    subtitle:" ",
    group:{gremlin:"in().has('lbl','alert').values('objectId').toList()[0]"},        
    shape:"square",      
    image:"",        
    borderRadius:"0", 
    borderWidth:"3",  
    borderDashes:[1,0],
    value:"0"        
    },
  actions:[
    {name:"Show", url:{gremlin:"id().next().toString().replaceFirst(\"^\", \"Node.jsp?id=\")"}, target:"result"}
    ]
  }
stylesheet.nodes.cutout = {
  properties:{gremlin:"valueMap('cutoutScienceFn', 'cutoutTemplateFn', 'cutoutDifferenceFn', 'cutoutScience', 'cutoutTemplate', 'cutoutDifference').toList()[0]"},
  graphics: {
    label:"cutout",
    title:"cutout",        
    subtitle:" ",
    group:{gremlin:"in().has('lbl', 'alert').values('objectId').toList()[0]"},        
    shape:"triangle",      
    image:"",        
    borderRadius:"0", 
    borderWidth:"3",  
    borderDashes:[1,0],
    value: {gremlin:"both().count().join().toString()"}        
    },
  actions:[                                                                 
    {name:"Science",    url:{js:"storeData(cutoutScienceFn,    cutoutScience)"               }, target:"image"   },
    {name:"Science",    url:{js:"storeData(cutoutScienceFn,    cutoutScience)"               }, target:"external"},
    {name:"Template",   url:{js:"storeData(cutoutTemplateFn,   cutoutTemplate)"              }, target:"image"   },
    {name:"Template",   url:{js:"storeData(cutoutTemplateFn,   cutoutTemplate)"              }, target:"external"},
    {name:"Difference", url:{js:"storeData(cutoutDifferenceFn, cutoutDifference)"            }, target:"image"   },
    {name:"Difference", url:{js:"storeData(cutoutDifferenceFn, cutoutDifference)"            }, target:"external"},
    {name:"Show", url:{gremlin:"id().next().toString().replaceFirst(\"^\", \"Node.jsp?id=\")"}, target:"result"}
    ]
  }
stylesheet.nodes.AlertsCollection = {
  properties:{gremlin:"valueMap('title').toList()[0]"},
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
    {name:"Show", url:{gremlin:"id().next().toString().replaceFirst(\"^\", \"Node.jsp?id=\")"}, target:"result"}
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
