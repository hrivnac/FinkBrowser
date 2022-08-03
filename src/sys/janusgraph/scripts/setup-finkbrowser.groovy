class FinkBrowserIJCLab {

  def static init() {
    clientH = new com.Lomikel.HBaser.HBaseClient("@STORAGE.HOSTNAME@", @STORAGE.HBASE.PORT@)
    clientH.connect("@STORAGE.HBASE.TABLE@", "@STORAGE.HBASE.SCHEMA@")
    com.Lomikel.Januser.Hertex.setHBaseClient(clientH)    
    com.Lomikel.HBaser.HBaseClient.registerVertexType("alert", com.astrolabsoftware.FinkBrowser.Januser.Alert.class)
    println "class FinkBrowser IJCLab initialised"
    }

  def static hi() {
    return "Hello World from FinkBrowser server !"
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
    return 'candidates(g, objectId)';
    }
    
  def static candidates(g, objectId) {
    return g.V().has('objectId', objectId).out().has('lbl', 'candidate');
    }  
    
  def static help() {
    return geosearch_help()    + "\n"
         + drop_by_date_help() + "\n"
         + importStatus_help() + "\n"
         + candidates_help();
    }

  def static clientH  
    
  }

FinkBrowserIJCLab.init()  