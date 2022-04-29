//clientH = new com.Lomikel.HBaser.HBaseClient("@STORAGE.HOSTNAME@", @STORAGE.HBASE.PORT@)
//clientH.connect("@STORAGE.HBASE.TABLE@", "@STORAGE.HBASE.SCHEMA@")
//com.Lomikel.Januser.Hertex.setHBaseClient(clientH)
//
//com.Lomikel.HBaser.HBaseClient.registerVertexType("alert", com.astrolabsoftware.FinkBrowser.Januser.Alert.class)

def hi_finkbrowser() {
  return "Hello World from FinkBrowser server !"
  }


def geosearch_help() {
  return 'geosearch(ra, dec, ang[deg], jdmin, jdmax, limit)'
  }
  
def geosearch(ra, dec, ang, jdmin, jdmax, limit) {
  lat = dec
  lon = ra - 180
  dist = ang * 6371.0087714 * Math.PI / 180
  nDir = g.V().has('direction', geoWithin(Geoshape.circle(lat, lon, dist))).count().next()
  nJD  = g.V().has('direction', geoWithin(Geoshape.circle(lat, lon, dist))).limit(nDir).has('jd', inside(jdmin, jdmax)).count().next()
  if (limit < nJD) {
    nJD = limit
    }
  return g.V().has('direction', geoWithin(Geoshape.circle(lat, lon, dist))).limit(nDir).has('jd', inside(jdmin, jdmax)).limit(nJD)
  }

def drop_by_date_help() {
  return 'drop_by_date(graph, importDate, nCommit, tWait)'
  }
  
def drop_by_date(importDate, nCommit, tWait) {
  //importDate = 'Mon Feb 14 05:51:20 UTC 2022'
  //nCommit = 500
  i = 0
  tot = 0
  nMax=g.V().has('importDate', importDate).count().next()
  println('' + nMax + ' vertexes to drop')
  t0 = System.currentTimeMillis()
  while(true) {
    g.V().has('importDate', importDate).limit(nCommit).out().out().drop().iterate()
    g.V().has('importDate', importDate).limit(nCommit).out().drop().iterate()
    g.V().has('importDate', importDate).limit(nCommit).drop().iterate()
    graph.traversal().tx().commit()
    Thread.sleep(tWait)
    tot = nCommit * ++i
    dt = (System.currentTimeMillis() - t0) / 1000
    per = 100 * tot / nMax
    freq = tot / dt
    rest = (nMax - tot) / freq / 60 /60
    println(tot + ' = ' + per + '% at ' + freq + 'Hz, ' + rest + 'h to go')
    }
  }
  
def importStatus_help() {
  return 'importStatus(g)'
  }
    
def importStatus() {
  txt = ''
  txt += 'Imported:\n'
  g.V().has('lbl', 'Import').has('nAlerts', neq(0)).order().by('importSource').valueMap('importSource', 'importDate', 'nAlerts').each{txt += '\t' + it + '\n'}
  txt += 'Importing:\n'
  g.V().has('lbl', 'Import').hasNot('complete').order().by('importSource').valueMap('importSource', 'importDate').each{txt += '\t' + it + '\n'}
  return txt
  }
  
def candidates_help() {
  return 'candidates(g, objectId)';
  }
  
def candidates(g, objectId) {
  return g.V().has('objectId', objectId).out().has('lbl', 'candidate');
  }  
  
def help() {
  geosearch_help();
  drop_by_date_help();
  importStatus_help();
  candidates_help();
  }
