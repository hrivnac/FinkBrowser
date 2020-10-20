package com.astrolabsoftware.FinkBrowser.HBaser;

// Lomikel
import com.Lomikel.HBaser.HBaseClient;

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
                                                 boolean iftime) throws IOException {
    Map<String, String> searchMap = jd2keys(jdStart, jdStop);
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
    *           in the format expected for the scan methods.
    * @throws IOException If anything fails. */
  // TBD: refactor
  public Map<String, String> jd2keys(String jd) throws IOException {
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
    Map<String, String> searchMap = new TreeMap<>();
    searchMap.put("key:key", keys);
    return searchMap;
    }
   
  /** Give all objectIds between two specified Julian Dates (inclusive).
    * @param jdStart The start Julian Data (with day fraction), evaluated as literal prefix scan.
    * @param jdStart The stop Julian Data (with day fraction), evaluated as literal prefix scan.
    * @return   The {@link Map} of corresponding keys of the main table,
    *           in the format expected for the scan methods.
    * @throws IOException If anything fails. */
  public Map<String, String> jd2keys(String jdStart,
                                     String jdStop) throws IOException {
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
    Map<String, String> searchMap = new TreeMap<>();
    searchMap.put("key:key", keys);
    return searchMap;
    }
    
  /** Give the timeline for the column. It makes use of the Julian Date alert time
    * instead of HBase timestamp. 
    * @param columnName The name of the column.
    * @param search   The search terms as <tt>family:column:value,...</tt>.
    *                 Key can be searched with <tt>family:column = key:key<tt> "pseudo-name".
    *                 {@link Comparator} can be chosen as <tt>family:column:value:comparator</tt>
    *                 among <tt>exact,prefix,substring,regex</tt>.
    *                 The default for key is <tt>prefix</tt>,
    *                 the default for columns is <tt>substring</tt>.
    *                 It can be <tt>null</tt>.
    *                 All searches are executed as prefix searches.    
    * @return           The {@link Map} value-JulianDate. */
  @Override
  public Map<String, Number> timeline(String columnName,
                                      String search) {
    Map<String, Number> tl = new TreeMap<>();
    Map<String, Map<String, String>> results = scan(null, search, columnName + ",i:jd", 0, false, false);
    for (Map.Entry<String, Map<String, String>> entry : results.entrySet()) {
      if (!entry.getKey().startsWith("schema")) { 
        tl.put(entry.getValue().get(columnName), Double.parseDouble(entry.getValue().get("i:jd")));
        }
      }
    return tl;
    }
    
  /** Give all recent values of the column. It makes use of the Julian Date alert time
    * instead of HBase timestamp. 
    * @param columnName     The name of the column.
    * @param substringValue The column value substring to search for.
    * @param minutes        How far into the past it should search. 
    * @param getValues      Whether to get column values or row keys.
    * @return               The {@link Set} of different values of that column. */
  @Override
  public Set<String> latests(String columnName,
                             String substringValue,
                             long minutes,
                             boolean getValues) {
    Set<String> l = new TreeSet<>();
    String search = "";
    if (substringValue != null) {
      search += columnName + ":" + substringValue;
      }
    Map<String, Map<String, String>> results = scan(null, search, null, minutes, false, false);
    for (Map.Entry<String, Map<String, String>> entry : results.entrySet()) {
      if (!entry.getKey().startsWith("schema")) {
        l.add(getValues ? entry.getValue().get(columnName) : entry.getKey());
        }
      }
    return l;
    }
  
  /** Logging . */
  private static Logger log = Logger.getLogger(FinkHBaseClient.class);

  }
