package com.astrolabsoftware.FinkBrowser.Januser;

//
import com.Lomikel.Utils.Init;
import com.Lomikel.Utils.Info;
import com.Lomikel.DB.Schema;
import com.Lomikel.Utils.LomikelException;
import com.Lomikel.HBaser.HBaseClient;
import com.Lomikel.HBaser.HBaseSchema;
import com.Lomikel.Phoenixer.PhoenixProxyClient;
import com.Lomikel.Phoenixer.PhoenixSchema;
import com.Lomikel.Januser.JanusClient;

// Tinker Pop
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.structure.Direction;

// Janus Graph
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;

// HBase
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName ;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.filter.Filter;  
import org.apache.hadoop.hbase.filter.FilterList;  
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;  
import org.apache.hadoop.hbase.filter.RowFilter;  
import org.apache.hadoop.hbase.filter.PrefixFilter;  
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.MultiRowRangeFilter;
import org.apache.hadoop.hbase.filter.MultiRowRangeFilter.RowRange;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.Cell;

// Java
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// Log4J
import org.apache.log4j.Logger;

/** <code>StructureCreator</code> generates the network of higher level entities
  * from the LSST {@link Alert}s.
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
public class StructureCreator extends JanusClient {

  /** TBD */
  public static void main(String[] args) throws Exception {
    Init.init();
    if (args[0].trim().equals("populate")) {
      String failedKey = args[9];
      do {
        failedKey = new StructureCreator(args[1]).populateGraphFromHBase(            args[2],
                                                                         new Integer(args[3]),
                                                                                     args[4],
                                                                                     args[5],
                                                                                     args[6],
                                                                                     args[7],
                                                                                     args[8],
                                                                                     failedKey,
                                                                                     args[10],
                                                                         new Integer(args[11]),
                                                                         new Integer(args[12]),
                                                                         new Integer(args[13]),
                                                                                     args[14].equals("true"),
                                                                                     args[15].equals("true"),
                                                                                     args[16].equals("true"));
        }
      while (!failedKey.equals(""));
      }                             
    else {
      System.err.println("Unknown function " + args[0] + ", try or populate");
      System.exit(-1);
      }
    } 
    
  /** Create with default parameters. */
  public StructureCreator() {
    super();
    }
   
  /** Create with connection parameters.
    * @param hostname The HBase hostname.
    * @param table    The HBase table. */
  public StructureCreator(String hostname,
                          String table) {
    super(hostname, table, false);
    }
   
  /** Create with connection parameters.
    * @param hostname The HBase hostname.
    * @param table    The HBase table.
    * @param batch    Whether open graph for batch loading. */
  public StructureCreator(String  hostname,
                          String  table,
                          boolean batch) {
    super(hostname, table, batch);
    }
    
  /** Create with connection properties file.
    * @param properties The file with the complete properties. */
  public StructureCreator(String properties) {
    super(properties);
    }
      
  /** Populate JanusGraph from HBase table.
    * @param hbaseHost       The HBase hostname.
    * @param hbasePort       The HBase port.
    * @param hbaseTable      The HBase table to replicate in Graph.
    * @param tableSchema     The HBase table schema name.
    * @param label           The label of newly created Vertexes.
    * @param rowkey          The row key name.
    * @param keyPrefixSearch The key prefix to limit replication to.
    * @param keyStart        The key to start search from, may be blank.
    * @param keyStop         The key to stop search at, may be blank.
    * @param limit           The maximal number of entries to process (-1 means all entries).
    * @param skip            The number of entries to skip (-1 or 0 means no skipping).
    * @param commitLimit     The number of events to commit in one step (-1 means commit only at the end).
    * @param reset           Whether remove all {@link Vertex}es with the define
    *                        label before populating or check for each one and only
    *                        create it if it doesn't exist yet.
    * @param getOrCreate     Whether check the existence of the vertex before creating it.
    *                        (Index-based verification is disabled for speed.)
    * @param fullFill        Whether fill all variables or just rowkey and lbl.
    * @return                Blank if the population has been executed correctly, the last
    *                        sucessfull key otherwise.
    * @throws LomikelException If anything goes wrong. */
  // TBD: allow replacing, updating
  // TBD: read only rowkey if fullFill = false
  // TBD: handle binary columns
  public String populateGraphFromHBase(String  hbaseHost,
                                       int     hbasePort,
                                       String  hbaseTable,
                                       String  tableSchema,
                                       String  label,
                                       String  rowkey,
                                       String  keyPrefixSearch,
                                       String  keyStart,
                                       String  keyStop,
                                       int     limit,
                                       int     skip,
                                       int     commitLimit,
                                       boolean reset,
                                       boolean getOrCreate,
                                       boolean fullFill) throws LomikelException {
    log.info("Populating Graph from " + hbaseTable + "(" + tableSchema + ")@" + hbaseHost + ":" + hbasePort);
    log.info("\tvertex labels: " + label);
    log.info("\t" + rowkey + " starting with " + keyPrefixSearch);
    log.info("\tlimit/skip/commitLimit: " + limit + "/" + skip + "/" + commitLimit);
    if (reset) {
      log.info("\tcleaning before population");
      }
    if (getOrCreate) {
      log.info("\tadd vertex only if non-existent");
      }
    else {
      log.info("\tadd vertex even if it already exists");
      }
    if (fullFill) {
      log.info("\tfilling all variables");
      }
    else {
      log.info("\tfilling only " + rowkey + " and lbl");
      }
    if (!keyStart.equals("")) {
      log.info("Staring at " + keyStart);
      }
    if (!keyStop.equals("")) {
      log.info("Stopping at " + keyStop);
      }
    timerStart();
    if (reset) {                        
      log.info("Cleaning Graph, vertexes: " + label);
      g().V().has("lbl", label).drop().iterate();
      }
    commit();
    log.info("Connection to HBase table");
    HBaseClient hc = new HBaseClient(hbaseHost, hbasePort);
    hc.connect(hbaseTable, tableSchema); 
    hc.setLimit(0);
    String searchS = "key:key:" + keyPrefixSearch + ":prefix";
    if (!keyStart.equals("")) {
      searchS += ",key:startKey:" + keyStart;
      }
    if (!keyStop.equals("")) {
      searchS += ",key:stopKey:" + keyStop;
      }
    hc.scan(null, searchS, "*", 0, false, false);
    ResultScanner rs = hc.resultScanner();
    Schema schema = hc.schema();
    log.info("Populating Graph");
    Vertex v;
    String key;
    String lastInsertedKey = null;
    String failedKey       = null;
    String family;
    String field;
    String column;
    String value;
    int i = 0;
    //for (Result r : rs) {
    //  i++;
    //  key = Bytes.toString(r.getRow());
    //  for (Cell cell : r.listCells()) {
    //    family = Bytes.toString(cell.getFamilyArray(),    cell.getFamilyOffset(),    cell.getFamilyLength());
    //    column = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
    //    value  = Bytes.toString(cell.getValueArray(),     cell.getValueOffset(),     cell.getValueLength());
    //    }
    //  timer(label + "s created", i, 100, commitLimit);
    //  } 
    NavigableMap<byte[], NavigableMap<byte[], byte[]>>	 resultMap;
    try {
      for (Result r : rs) {
        resultMap = r.getNoVersionMap();
        key = Bytes.toString(r.getRow());
        if (!key.startsWith("schema")) {
          if (failedKey == null) {
            failedKey = key;
            }
          i++;
          if (i <= skip) {
            continue;
            }
          if (limit > 0 && i > limit) {
            break;
            }
          if (getOrCreate) {
            v = getOrCreate(label, rowkey, key);
            }
          else {
            v = g().addV(label).property(rowkey, key).property("lbl", label).next();
            }
          if (fullFill) {
            for (Map.Entry<byte[], NavigableMap<byte[], byte[]>> entry : resultMap.entrySet()) {
              family = Bytes.toString(entry.getKey());
              if (!family.equals("b")) {
                for (Map.Entry<byte[], byte[]> e : entry.getValue().entrySet()) {
                  field = Bytes.toString(e.getKey());
                  column = family + ":" + field;
                  if (schema != null) {
                    value = schema.decode(column, e.getValue());
                    }
                  else {
                    value = Bytes.toString(e.getValue());
                    }
                  v.property(field, value);
                  }
                }
              }
            }
          }
        if (timer(label + "s created", i - 1, 100, commitLimit)) {
          rs.renewLease();
          lastInsertedKey = key;
          failedKey       = null;
          }
        }
      }
    catch (Exception e) {
      log.fatal("Failed while inserting " + i + "th vertex,\tlast inserted vertex: " + lastInsertedKey + "\tfirst uncommited vertex: " + failedKey, e);
      close();
      hc.close();
      return lastInsertedKey;
      }
    timer(label + "s created", i - 1, -1, -1);
    commit();
    close();
    hc.close();
    return "";
    }
    
  /** Logging . */
  private static Logger log = Logger.getLogger(StructureCreator.class);

  }
