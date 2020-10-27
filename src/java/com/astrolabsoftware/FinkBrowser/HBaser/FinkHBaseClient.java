package com.astrolabsoftware.FinkBrowser.HBaser;

// Lomikel
import com.Lomikel.HBaser.HBaseClient;
import com.Lomikel.Utils.DateTimeManagement;
import com.Lomikel.Utils.Pair;

// HealPix
import cds.healpix.Healpix;
import cds.healpix.HealpixNested;
import cds.healpix.HealpixNestedFixedRadiusConeComputer;
import cds.healpix.HealpixNestedBMOC;
import cds.healpix.FlatHashIterator;

// Java
import java.lang.Math;
import static java.lang.Math.PI;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.io.IOException;

// Log4J
import org.apache.log4j.Logger;

/** <code>FinkHBaseClient</code> handles connectionto HBase table
  * with specific Fink functionality. 
  * It expects the main table with schema and <code>key = alert.jd</code>
  * and the schemaless index table with <code>key = jd.alert</code> and one
  * <code>column = i:objectId</code>.
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
public class FinkHBaseClient extends HBaseClient {
   
  /** Create.
    * @param zookeepers The coma-separated list of zookeper ids.
    * @param clientPort The client port. 
    * @throws IOException If anything goes wrong. */
  public FinkHBaseClient(String zookeepers,
                         String clientPort) throws IOException {
    super(zookeepers, clientPort);
    }
       
  /** Create.
    * @param zookeepers The coma-separated list of zookeper ids.
    * @param clientPort The client port. 
    * @throws IOException If anything goes wrong. */
  public FinkHBaseClient(String zookeepers,
                         int    clientPort) throws IOException {
    super(zookeepers, clientPort);
    }
   
  /** Create.
    * @param url The HBase url.
    * @throws IOException If anything goes wrong. */
  public FinkHBaseClient(String url) throws IOException {
    super(url);
    }
   
  /** Create on <em>localhost</em>.
    * @throws IOException If anything goes wrong. */
  public FinkHBaseClient() throws IOException {
    super();
    }

  /** Get alerts between two Julian dates (inclusive).
    * @param jdStart   The starting Julian date (including day franction).
    * @param jdStop    The stopping Julian date (including day franction).
    * @param filter    The names of required values as <tt>family:column,...</tt>.
    *                  It can be <tt>null</tt>.
    * @param ifkey     Whether give also entries keys.
    * @param iftime    Whether give also entries timestamps.
    * @return          The {@link Map} of {@link Map}s of results as <tt>key-&t;{family:column-&gt;value}</tt>. */
  public Map<String, Map<String, String>> search(String  jdStart,
                                                 String  jdStop,
                                                 String  filter,
                                                 boolean ifkey,
                                                 boolean iftime)  {
    log.info("Searching for alerts in jd interval: " + jdStart + " - " + jdStop);
    Map<String, String> searchMap = jd2keys(jdStart, jdStop);
    if (searchMap.isEmpty()) {
      return new TreeMap<String, Map<String, String>>();
      }
    return scan(null,
                searchMap,
                filter,
                0,
                0,
                ifkey,
                iftime);
    }
    
   /** Get alerts within a spacial cone (inclusive).
    * @param ra     The central value of ra (in deg).
    * @param dec    The central value of dec (in deg).
    * @param delta  The maximal angular distance from the central direction (in deg).
    * @param filter The names of required values as <tt>family:column,...</tt>.
    *               It can be <tt>null</tt>.
    * @param ifkey  Whether give also entries keys.
    * @param iftime Whether give also entries timestamps.
    * @return       The {@link Map} of {@link Map}s of results as <tt>key-&t;{family:column-&gt;value}</tt>. */
  public Map<String, Map<String, String>> search(double  ra,
                                                 double  dec,
                                                 double  delta,
                                                 String  filter,
                                                 boolean ifkey,
                                                 boolean iftime)  {
    log.info("Searching for alerts within " + delta + " deg of (ra, dec) = (" + ra + ", " + dec + ")");
    Map<String, String> searchMap = radec2keys(ra, dec, delta);
    if (searchMap.isEmpty()) {
      return new TreeMap<String, Map<String, String>>();
      }
    return scan(null,
                searchMap,
                filter,
                0,
                0,
                ifkey,
                iftime);
    }
  
  /** Give all objectIds corresponding to specified Julian Date.
    * It uses *.jd table.
    * @param jd The Julian Data (with day fraction).
    * @return   The {@link Map} of corresponding keys of the main table,
    *           in the format expected for the scan methods. */
  public Map<String, String> jd2keys(String jd) {
    Map<String, String> searchMap = new TreeMap<>();
    try {
      HBaseClient client = new HBaseClient(zookeepers(), clientPort());
      client.connect(tableName() + ".jd", null);
      Map<String, Map<String, String>> results = client.scan(null,
                                                             "key:key:" + jd,
                                                             null,
                                                             0,
                                                             0,
                                                             false,
                                                             false);
      String keys = results.values().stream().map(m -> m.get("i:objectId")).collect(Collectors.joining(","));
      if (keys != null && !keys.trim().equals("")) { 
        searchMap.put("key:key", keys);
        }
      client.close();
      }
    catch (IOException e) {
      log.error("Cannot search", e);
      }
    return searchMap;
    }
   
  /** Give all objectIds between two specified Julian Dates (inclusive).
    * It uses *.jd table.
    * @param jdStart The start Julian Data (with day fraction), evaluated as literal prefix scan.
    * @param jdStart The stop Julian Data (with day fraction), evaluated as literal prefix scan.
    * @return   The {@link Map} of corresponding keys of the main table,
    *           in the format expected for the scan methods. */
  public Map<String, String> jd2keys(String jdStart,
                                     String jdStop)  {
    Map<String, String> searchMap = new TreeMap<>();
    try {
      HBaseClient client = new HBaseClient(zookeepers(), clientPort());
      client.connect(tableName() + ".jd", null);
      client.setRangeScan(true);
      Map<String, Map<String, String>> results = client.scan(null,
                                                             "key:key:" + jdStart + ":prefix," + "key:key:" + jdStop + ":prefix",
                                                             null,
                                                             0,
                                                             0,
                                                             false,
                                                             false);
      String keys = results.values().stream().map(m -> m.get("i:objectId")).collect(Collectors.joining(","));
      if (keys != null && !keys.trim().equals("")) { 
        searchMap.put("key:key", keys);
        }
      client.close();
      }
    catch (IOException e) {
      log.error("Cannot search", e);
      }
    return searchMap;
    }
    
  /** Give all objectIds within a spacial cone.
    * It uses *.pixel table.
    * @param ra    The central value of ra/lon  (in deg).
    * @param dec   The central value of dec/lat (in deg).
    * @param delta The maximal angular distance from the central direction (in deg).
    * @return      The {@link Map} of corresponding keys of the main table,
    *              in the format expected for the scan methods. */
  public Map<String, String> radec2keys(double ra,
                                        double dec,
                                        double delta)  {
    int nside = 131072;
    int depth = Healpix.depth(nside);
    double coneCenterLon = Math.toRadians(ra);
    double coneCenterLat = PI / 2.0 - Math.toRadians(dec);
    double coneRadiusDel = Math.toRadians(delta);
    HealpixNested hn = Healpix.getNested(depth);
    //HealpixNestedFixedRadiusConeComputer cc = hn.newConeComputer(coneRadiusDel);     // beta code!!
    HealpixNestedFixedRadiusConeComputer cc = hn.newConeComputerApprox(coneRadiusDel); // robust code
    HealpixNestedBMOC bmoc = cc.overlappingCells(coneCenterLon, coneCenterLat);
    FlatHashIterator hIt = bmoc.flatHashIterator();
    String pixs = "";
    while (hIt.hasNext()) {
      pixs += hIt.next() + ",";
      }
    Map<String, String> pixMap = new TreeMap<>();
    pixMap.put("key:key", pixs);
    Map<String, String> searchMap = new TreeMap<>();
    try {
      HBaseClient client = new HBaseClient(zookeepers(), clientPort());
      client.connect(tableName() + ".pixel", null);
      Map<String, Map<String, String>> results = client.scan(null,
                                                             pixMap,
                                                             null,
                                                             0,
                                                             0,
                                                             false,
                                                             false);
      String keys = results.values().stream().map(m -> m.get("i:objectId")).collect(Collectors.joining(","));
      if (keys != null && !keys.trim().equals("")) { 
        searchMap.put("key:key:prefix", keys);
        }
      client.close();
      }
    catch (IOException e) {
      log.error("Cannot search", e);
      }
    return searchMap;
    }
    
  /** Give the timeline for the column. It makes use of the Julian Date alert time
    * instead of HBase timestamp. 
    * @param columnName The name of the column.
    * @param search     The search terms as <tt>family:column:value,...</tt>.
    *                   Key can be searched with <tt>family:column = key:key<tt> "pseudo-name".
    *                   {@link Comparator} can be chosen as <tt>family:column:value:comparator</tt>
    *                   among <tt>exact,prefix,substring,regex</tt>.
    *                   The default for key is <tt>prefix</tt>,
    *                   the default for columns is <tt>substring</tt>.
    *                   It can be <tt>null</tt>.
    *                   All searches are executed as prefix searches.    
    * @return         The {@link Set} of {@link Pair}s of JulianDate-value. */
  @Override
  public Set<Pair<String, String>> timeline(String columnName,
                                            String search) {
    Set<Pair<String, String>> tl = new TreeSet<>();
    Map<String, Map<String, String>> results = scan(null, search, columnName + ",i:jd", 0, false, false);
    Pair<String, String> p;
    for (Map.Entry<String, Map<String, String>> entry : results.entrySet()) {
      if (!entry.getKey().startsWith("schema")) {
        p = Pair.of(entry.getValue().get("i:jd"    ),
                    entry.getValue().get(columnName));
        tl.add(p);
        }
      }
    return tl;
    }
    
  /** Give all recent values of the column. It makes use of the Julian Date alert time
    * instead of HBase timestamp. 
    * @param columnName     The name of the column.
    * @param prefixValue    The column value prefix to search for.
    * @param minutes        How far into the past it should search. 
    * @param getValues      Whether to get column values or row keys.
    * @return               The {@link Set} of different values of that column. */
  public Set<String> latests(String  columnName,
                             String  prefixValue,
                             long    minutes,
                             boolean getValues) {
    Set<String> l = new TreeSet<>();
    double nowJD = DateTimeManagement.julianDate();
    double minJD = nowJD - minutes / 60.0 / 24.0;
    Map<String, Map<String, String>> results = search(String.valueOf(minJD),
                                                      String.valueOf(nowJD),
                                                      columnName,
                                                      false,
                                                      false);
    for (Map.Entry<String, Map<String, String>> entry : results.entrySet()) {
      l.add(getValues ? entry.getValue().get(columnName) : entry.getKey());
      }
    return l;
    }
  
  /** Logging . */
  private static Logger log = Logger.getLogger(FinkHBaseClient.class);

  }
