// Graph stylesheet
var stylesheet = {
  nodes: {
    "default": {
      graphics: {
        label:{gremlin:"properties('lbl').value()"},         // can be "", can be {gremlin:...}
        title:" ",         // can be "", can be {gremlin:...}
        subtitle:" ",      // can be "", can be {gremlin:...}
        group:" ",         // specifies graphics properties, can be "", can be {gremlin:...}
        shape:"dot",       // in text:  ellipse, circle, database, box, text
                           // out text: image, circularImage, diamond, dot, star, triangle, triangleDown, hexagon, square, icon
                           // can be {js:...}
        image:" ",         // should be present only if shape:"image"
        borderRadius:"0",  // should be present only if shape:"box"
        borderWidth:"1",
        borderDashes:[1,0],
        value:"0"          // can be {gremlin:...}
        },
      actions:[
        {name:"info", url:"https://astrolabsoftware.github.io"}
        ]
      },
    "LAL": {
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
      },
    "Local": {
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
      },
    "AstroLabNet": {
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
    },
  edges: {
    "default": {
      graphics: {
        label:" ",
        title:" ",
        subtitle:" ",
        arrows:"middle",
        value:"0",
        group:" "
        },
      actions: [
        {name:"info", url:"https://astrolabsoftware.github.io"}
        ]
      },
    "has": {
      graphics: {
        label:" ",
        title:" ",
        subtitle:" ",
        arrows:"to",
        value:"0",
        group:" "
        },
      actions: [
        {name:"info", url:"https://astrolabsoftware.github.io"}
        ]
      }
    }
  }
 
