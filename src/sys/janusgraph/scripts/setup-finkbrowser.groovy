class FinkBrowserServer {

  def static init() {
    clientH = new com.Lomikel.HBaser.HBaseClient("@STORAGE.HOSTNAME@", @STORAGE.HBASE.PORT@)
    clientH.connect("@STORAGE.HBASE.TABLE@", "@STORAGE.HBASE.SCHEMA@")
    com.Lomikel.Januser.Hertex.setHBaseClient(clientH)    
    com.Lomikel.HBaser.HBaseClient.registerVertexType("alert", com.astrolabsoftware.FinkBrowser.Januser.Alert.class)
    println "class FinkBrowser Server initialised"
    }

  def static hi() {
    return "Hello World from FinkBrowser Server !"
    }

  def static getDataLink_help() {
    return 'getDataLink(v, q)'
    }
   
  def static getDataLink(v, q = null) {
    def url   = v.values('url'  ).next()
    def query
    if (q != null) {
      query = q
      }
    else if (v.values('query').hasNext()) {
      query = v.values('query').next()
      }
    else {
      return 'no Query'
      }
    switch (v.values('technology').next()) {
      case 'HBase':
        def (hostname, port, table, schema) = url.split(':') // 134.158.74.54:2181:ztf:schema_0.7.0_0.3.8
        def client = new com.astrolabsoftware.FinkBrowser.HBaser.FinkHBaseClient(hostname, port)
        client.connect(table, schema)
        query = query.toString()
        return Eval.me('client', client, query)
        break
      case 'Graph':
        def (backend, hostname, port, table) = url.split(':') // hbase:188.184.87.217:8182:janusgraph
        def graph = JanusGraphFactory.build().
                                      set('storage.backend',     backend ).
                                      set('storage.hostname',    hostname).
                                      set('storage.port',        port    ).
                                      set('storage.hbase.table', table   ).
                                      open()
        def g = graph.traversal()
        return Eval.me('g', g, query)
        break
      case 'Phoenix':
        return groovy.sql.Sql.newInstance(url, 'org.apache.phoenix.jdbc.PhoenixDriver').
                              rows(query)
        break
      default:
        return 'unknown DataLink ' + v
        }
      }
  
   def static geosearch_help() {
     return 'geosearch(g, ra, dec, ang[deg], jdmin, jdmax, limit)'
     }
    
  def static geosearch(g, ra, dec, ang, jdmin, jdmax, limit) {
    def lat = dec
    def lon = ra - 180
    def dist = ang * 6371.0087714 * Math.PI / 180
    def nDir = g.V().has('direction', geoWithin(Geoshape.circle(lat, lon, dist))).count().next()
    def nJD  = g.V().has('direction', geoWithin(Geoshape.circle(lat, lon, dist))).limit(nDir).has('jd', inside(jdmin, jdmax)).count().next()
    if (limit < nJD) {
      nJD = limit
      }
    return g.V().has('direction', geoWithin(Geoshape.circle(lat, lon, dist))).limit(nDir).has('jd', inside(jdmin, jdmax)).limit(nJD)
    }
  
  def static drop_by_date_help() {
    return 'drop_by_date(graph, importDate, nCommit, tWait)'
    }
    
  def static drop_by_date(graph, importDate, nCommit, tWait) {
    def g = graph.traversal()
    //importDate = 'Mon Feb 14 05:51:20 UTC 2022'
    //nCommit = 500
    def i = 0
    def tot = 0
    def nMax = g.V().has('importDate', importDate).count().next()
    println('' + nMax + ' vertexes to drop')
    def t0 = System.currentTimeMillis()
    while(true) {
      g.V().has('importDate', importDate).limit(nCommit).out().out().drop().iterate()
      g.V().has('importDate', importDate).limit(nCommit).out().drop().iterate()
      g.V().has('importDate', importDate).limit(nCommit).drop().iterate()
      graph.traversal().tx().commit()
      Thread.sleep(tWait)
      tot = nCommit * ++i
      def dt = (System.currentTimeMillis() - t0) / 1000
      def per = 100 * tot / nMax
      def freq = tot / dt
      def rest = (nMax - tot) / freq / 60 /60
      println(tot + ' = ' + per + '% at ' + freq + 'Hz, ' + rest + 'h to go')
      }
    }
    
  def static importStatus_help() {
    return 'importStatus(g)'
    }
      
  def static importStatus(g) {
    def txt = ''
    txt += 'Imported:\n'
    g.V().has('lbl', 'Import').has('nAlerts', neq(0)).order().by('importSource').valueMap('importSource', 'importDate', 'nAlerts').each{txt += '\t' + it + '\n'}
    txt += 'Importing:\n'
    g.V().has('lbl', 'Import').hasNot('complete').order().by('importSource').valueMap('importSource', 'importDate').each{txt += '\t' + it + '\n'}
    return txt
    }
    
  def static candidates_help() {
    return 'candidates(g, objectId)'
    }
    
  def static candidates(g, objectId) {
    return g.V().has('objectId', objectId).out().has('lbl', 'candidate')
    }  
    
  def static help() {
    return getDataLink_help()  + "\n" +
           geosearch_help()    + "\n" +
           drop_by_date_help() + "\n" +
           importStatus_help() + "\n" +
           candidates_help()
    }

  def static clientH  
    
  }

FinkBrowserServer.init()  