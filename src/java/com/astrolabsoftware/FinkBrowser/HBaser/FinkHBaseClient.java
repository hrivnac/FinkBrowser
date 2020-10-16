package com.astrolabsoftware.FinkBrowser.HBaser;

// Lomikel
import com.Lomikel.HBaser.HBaseClient;

// HBase
import org.apache.hadoop.hbase.client.Table;

// Java
import java.util.Map;
import java.util.TreeMap;
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
   
  /** Give all objectIds corresponding to specified Julian Date.
    * @param jd The Julian Data (with day fraction).
    * @return   The {@link Map} of corresponding keys of the main table,
    *           in the format expected for the scan methods. */
  public Map<String, String> jd2keys(String jd) throws IOException {
    HBaseClient client = new HBaseClient(zookeepers(), clientPort());
    client.connect(tableName() + ".jd", null);
    Map<String, Map<String, String>> results = client.scan(null,
                                                     "key:key:t_" + jd,
                                                     null,
                                                     0,
                                                     false,
                                                     false);
    String keys = results.values().stream().map(m -> m.get("i:objectId")).collect(Collectors.joining(","));
    Map<String, String> searchMap = new TreeMap<>();
    searchMap.put("key:key", keys);
    return searchMap;
    }
   
  /** Give all objectIds between two specified Julian Dates (inclusive).
    * @param jdStart The start Julian Data (with day fraction).
    * @param jdStart The stop Julian Data (with day fraction).
    * @return   The {@link Map} of corresponding keys of the main table,
    *           in the format expected for the scan methods. */
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
  
  /** Logging . */
  private static Logger log = Logger.getLogger(FinkHBaseClient.class);

  }
