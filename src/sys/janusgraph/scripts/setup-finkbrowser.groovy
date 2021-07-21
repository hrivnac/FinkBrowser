clientH = new com.Lomikel.HBaser.HBaseClient("@STORAGE.HOSTNAME@", @STORAGE.HBASE.PORT@)
clientH.connect("@STORAGE.HBASE.TABLE@", "@STORAGE.HBASE.SCHEMA@")
com.Lomikel.Januser.Hertex.setHBaseClient(clientH)

com.Lomikel.HBaser.HBaseClient.registerVertexType("alert", com.astrolabsoftware.FinkBrowser.Januser.Alert.class)

def hi_finkbrowser() {
  return "Hello World from FinkBrowser !"
  }

def geosearch(lat, lon, radius, jdmin, jdmax, limit) {
  nDir = g.V().has('direction', geoWithin(Geoshape.circle(lat, lon, radius*6371.0087714*180/Math.PI))).count().next()
  nJD  = g.V().has('direction', geoWithin(Geoshape.circle(lat, lon, radius*6371.0087714*180/Math.PI))).limit(nDir).has('jd', inside(jdmin, jdmax)).count().next()
  if (limit < nJD) {
    nJD = limit
    }
  return g.V().has('direction', geoWithin(Geoshape.circle(lat, lon, radius*6371.0087714*180/Math.PI))).limit(nDir).has('jd', inside(jdmin, jdmax)).limit(nJD)
  }
  