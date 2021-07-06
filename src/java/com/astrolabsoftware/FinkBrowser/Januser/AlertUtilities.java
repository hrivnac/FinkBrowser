package com.astrolabsoftware.FinkBrowser.Januser;

import com.astrolabsoftware.FinkBrowser.Utils.Init;

// Lomikel
import com.Lomikel.DB.Schema;
import com.Lomikel.Utils.ByteArray;
import com.Lomikel.Utils.LomikelException;
import com.Lomikel.HBaser.HBaseClient;
import com.Lomikel.HBaser.HBaseSchema;
import com.Lomikel.Januser.JanusClient;
import com.Lomikel.Januser.GremlinRecipies;

// Tinker Pop
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;
import static org.apache.tinkerpop.gremlin.process.traversal.P.*;
import org.apache.tinkerpop.gremlin.structure.Vertex;

// Janus Graph
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.attribute.Geoshape;

// HBase
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.Cell;

// Java
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

// Log4J
import org.apache.log4j.Logger;

/** <code>AlertUtilities</code> provides utility searches for alerts.
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
public class AlertUtilities extends JanusClient {

  /** TBD */
  public static void main(String[] args) {
    AlertUtilities au = new AlertUtilities(args[0]);
    List<Vertex> r = au.searchJd(2459314.7213079, 2459314.722002344, "candidate");
    log.info(r);
    au.close();
    }
    
  /** Create with default parameters. */
  public AlertUtilities() {
    super();
    }
   
  /** Create with connection parameters.
    * @param hostname The HBase hostname.
    * @param table    The HBase table. */
  public AlertUtilities(String hostname,
                        String table) {
    super(hostname, table, false);
    }
   
  /** Create with connection parameters.
    * @param hostname The HBase hostname.
    * @param table    The HBase table.
    * @param batch    Whether open graph for batch loading. */
  public AlertUtilities(String  hostname,
                        String  table,
                        boolean batch) {
    super(hostname, table, batch);
    }
    
  /** Create with connection properties file.
    * @param properties The file with the complete properties. */
  public AlertUtilities(String properties) {
    super(properties);
    }
      
  /** TBD */
  public List<Vertex> searchJd(double since,
                               double till,
                               String lbl) {
    log.info("Searching " + lbl + " within " + since + " - " + till);
    return g().V().has("jd", inside(since, till)).has("lbl", lbl).toList();
    }
    
  /** Logging . */
  private static Logger log = Logger.getLogger(AlertUtilities.class);

  }
