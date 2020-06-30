def globals = [:]

globals << [hook : [
  onStartUp: { ctx -> ctx.logger.info("Executed once at startup of Gremlin Server.")},
  onShutDown: { ctx -> ctx.logger.info("Executed once at shutdown of Gremlin Server.")}
  ] as LifeCycleHook]
  
//globals << [graph : JanusGraphFactory.build().set("storage.backend", "hbase").set("storage.hostname", "134.158.74.54").set("storage.hbase.table", "janusgraph").open()]
globals << [g : graph.traversal()]
globals << [g1 : graph1.traversal()]
globals << [gTestJanusGraph : graphTestJanusGraph.traversal()]
globals << [gTestJanusGraph2 : graphTestJanusGraph2.traversal()]
globals << [gTestJGraph2 : graphTestJGraph2.traversal()]
globals << [gTestJGraph3 : graphTestJGraph3.traversal()]
//globals << [gTestJGraph4 : graphTestJGraph4.traversal()]
