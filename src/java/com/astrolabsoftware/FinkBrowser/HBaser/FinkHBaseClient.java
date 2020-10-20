package com.astrolabsoftware.FinkBrowser.HBaser;

// Lomikel
import com.Lomikel.HBaser.HBaseClient;
import com.Lomikel.Utils.DateTimeManagement;
import com.Lomikel.Utils.Pair;

// HBase
import org.apache.hadoop.hbase.client.Table;

// Java
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
   
  /** Give all objectIds corresponding to specified Julian Date.
    * @param jd The Julian Data (with day fraction).
    * @return   The {@link Map} of corresponding keys of the main table,
    *           in the format expected for the scan methods. */
  // TBD: refactor
  public Map<String, String> jd2keys(String jd) {
    Map<String, String> searchMap = new TreeMap<>();
    try {
      HBaseClient client = new HBaseClient(zookeepers(), clientPort());
      client.connect(tableName() + ".jd", null);
      Map<String, Map<String, String>> results = client.scan(null,
                                                             "key:key:t_" + jd,
                                                             null,
                                                             0,
                                                             0,
                                                             false,
                                                             false);
      String keys = results.values().stream().map(m -> m.get("i:objectId")).collect(Collectors.joining(","));
      searchMap.put("key:key", keys);
      }
    catch (IOException e) {
      log.error("Cannot search", e);
      }
    return searchMap;
    }
   
  /** Give all objectIds between two specified Julian Dates (inclusive).
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
                                                             "key:key:t_" + jdStart + "," + "key:key:t_" + jdStop,
                                                             null,
                                                             0,
                                                             false,
                                                             false);
      String keys = results.values().stream().map(m -> m.get("i:objectId")).collect(Collectors.joining(","));
      if (keys != null && !keys.trim().equals("")) { 
        searchMap.put("key:key", keys);
        }
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
    double minJD = nowJD - minutes / 69.0 / 24.0; // BUG: not correct
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
