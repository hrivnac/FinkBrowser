//clientH = new com.Lomikel.HBaser.HBaseClient("@STORAGE.HOSTNAME@", @STORAGE.HBASE.PORT@)
//clientH.connect("@STORAGE.HBASE.TABLE@", "@STORAGE.HBASE.SCHEMA@")
//com.Lomikel.Januser.Hertex.setHBaseClient(clientH)
//
//com.Lomikel.HBaser.HBaseClient.registerVertexType("alert", com.astrolabsoftware.FinkBrowser.Januser.Alert.class)

def hi_finkbrowser() {
  return "Hello World from FinkBrowser !"
  }

def geosearch_help() {
  return 'geosearch(ra, dec, ang[deg], jdmin, jdmax, limit)';
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
  