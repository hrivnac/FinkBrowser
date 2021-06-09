// why omit candid ?
// linked candidates


package com.astrolabsoftware.FinkBrowser.Avro;

import com.astrolabsoftware.FinkBrowser.Utils.Init;

// Lomikel
import com.Lomikel.Utils.Coding;
import com.Lomikel.Januser.JanusClient;
import com.Lomikel.Januser.GremlinRecipies;
import com.Lomikel.Utils.LomikelException;

// Avro
import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.Schema.Type;
import org.apache.avro.io.DatumReader;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericData.Array;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.specific.SpecificDatumReader;

// Tinker Pop
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;
import org.apache.tinkerpop.gremlin.structure.Vertex;

// Janus Graph
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.attribute.Geoshape;

// Java
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

// ZTF
//import ztf.alert.candidate;

// Log4J
import org.apache.log4j.Logger;

/** <code>AvroImporter</code> imports <em>Avro</em> files into <em>JanusGraph</em>.
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
// TBD: allow reset
// TBD: allow getOrCreate
public class AvroImporter extends JanusClient {
        
  /** Import Avro files or directory. 
    * @param args[0] The Janusgraph properties file. 
    * @param args[1] The Avro file or directory with Avro files.
    * @param args[2] The number of events to use for progress report (-1 means no report untill the end).
    * @param args[3] The number of events to commit in one step (-1 means commit only at the end).
    * @param args[4] The creation strategy. <tt>drop,replace,getOrCreate</tt>.
    * @throws LomikelException If anything goes wrong. */
   public static void main(String[] args) throws IOException {
    Init.init();
    if (args.length != 5) {
      log.error("AvroImporter.exe.jar <JanusGraph properties> [<file>|<directory>] <report limit> <commit limit> [create|reuse|drop]");
      System.exit(-1);
      }
    try {
      AvroImporter importer = new AvroImporter(            args[0],
                                               new Integer(args[2]),
                                               new Integer(args[3]),
                                                           args[4]);
      importer.timerStart();                    
      importer.process(args[1]);
      importer.commit();
      importer.close();
      }
    catch (LomikelException e) {
      log.fatal("Cannot import " + args[1] + " into " + args[0], e);
      System.exit(-1);
      }
    }
  
  /** Create with JanusGraph properties file.
    * @param properties  The file with the complete Janusgraph properties.
    * @param reportLimit The number of events to use for progress report (-1 means no report untill the end).
    * @param commitLimit The number of events to commit in one step (-1 means commit only at the end).
    * @param strategy    The creation strategy. <tt>drop,replace,getOrCreate</tt>. */
  public AvroImporter(String properties,
                      int    reportLimit,
                      int    commitLimit,
                      String strategy) {
    super(properties);
    log.info("Reporting after each " + reportLimit + " alerts");
    log.info("Committing after each " + commitLimit + " alerts");
    log.info("Using strategy: " + strategy);
    _reportLimit = reportLimit;
    _commitLimit = commitLimit;
    _create      = false;
    _reuse       = false;
    _replace     = false;
    _drop        = false;
    if (strategy.contains("create")) {
      _create = true;
      }
    if (strategy.contains("reuse")) {
      _reuse = true;
      }
    if (strategy.contains("replace")) {
      _replace = true;
      }
    if (strategy.contains("drop")) {
      _drop = true;
      }
    _gr = new GremlinRecipies(this);
    }
        
  /** Process directory with <em>Avro</em> alert files (recursive).
    * @param dirFN The dirname of directiory with data file.
    * @param fileExt The file extention.
     * @throws IOException      If problem with file reading. */
  public void processDir(String dirFN,
                         String fileExt) throws IOException {  
    log.info("Loading directory " + dirFN);
    File dir = new File(dirFN);
    int i = 0;
    for (String dataFN : dir.list()) {
      if (new File(dirFN + "/" + dataFN).isDirectory()) {
        processDir(dirFN + "/" + dataFN, "avro");
        }
      else if (dataFN.endsWith("." + fileExt)) {
        try {
          process(dirFN + "/" + dataFN);
          i++;
          }
        catch (IOException | LomikelException e) {
          log.error("Failed to process " + dirFN + "/" + dataFN, e);
          }
        }
      else {
        log.warn("Not " + fileExt + " file: " + dataFN);
        }
      }
    timer("alerts created", _n, -1, -1);      
    log.info("" + i + " files loaded");
    }
     
  /** Process <em>Avro</em> alert file or directory with files (recursive).
     * @param fn The filename of the data file
     *           or directory with files.
     * @throws IOException      If problem with file reading.
     * @throws LomikelException If anything wrong. */
  public void process(String fn) throws IOException, LomikelException {
    log.info("Loading " + fn);
    File file = new File(fn);
    if (file.isDirectory()) {
      processDir(fn, "avro");
      return;
      }
    else if (!file.isFile()) {
      log.error("Not a file/directory: " + fn);
      return;
      }
    processFile(file);
    }
    
  /** Process <em>Avro</em> alert file .
     * @param file The data file.
     * @throws IOException If problem with file reading. */
  public void processFile(File file) throws IOException {
    DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>();
    DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader);
    GenericRecord record = null;
    while (dataFileReader.hasNext()) {
      record = dataFileReader.next(record);
      processAlert(record);
      }
    dataFileReader.close();
    } 
   
  /** Process <em>Avro</em> alert.
    * @param record The full alert {@link GenericRecord}.
    * @return       The created {@link Vertex}. */
  public Vertex processAlert(GenericRecord record) {
    Map<String, String> values = getSimpleValues(record, getSimpleFields(record, new String[]{"objectId",
                                                                                              "candidate",
                                                                                              "prv_candidates",
                                                                                              "mulens",
                                                                                              "cutoutScience",
                                                                                              "cutoutTemplate",
                                                                                              "cutoutDifference"}));
    log.debug("alert:"); 
    Vertex v = vertex(record, "alert", "objectId");
    if (v != null) {
      for (Map.Entry<String, String> entry : values.entrySet()) {
        log.debug("\t" + entry.getKey() + " = " + entry.getValue());
        v.property(entry.getKey(), entry.getValue());
        }
      v.property("alertVersion", VERSION);
      }
    String ss;
    processGenericRecord((GenericRecord)(record.get("candidate")),
                         "candidate",
                         new String[]{"candid"},
                         true,
                         v,
                         "has");
    processGenericRecord((GenericRecord)(record.get("mulens")),
                         "mulens",
                         new String[]{},
                         false,
                         v,
                         "has");
    Array a = (Array)record.get("prv_candidates");
    if (a != null) {
      for (Object o : a) {
        processGenericRecord((GenericRecord)o,
                             "prv_candidate",
                             new String[]{"candid"},
                             true,
                             v,
                             "has");
        } 
      }
    for (String s : new String[]{"Science", "Template", "Difference"}) { 
      processCutout((GenericRecord)(record.get("cutout" + s)), "cutout" + s, v);
      }
    timer("alerts processed", ++_n, _reportLimit, _commitLimit);      
    return v;
    }
   
  /** Process <em>Avro</em> {@link GenericRecord}.
    * @param record       The {@link GenericRecord} to process.
    * @param name         The name of new {@link Vertex}.
    * @param avoids       The fields to not process.
    * @param tryDirection Whether try created <em>Direction</em> propertye from <em>ra,dec</em> fields.
    * @param mother       The mother {@link Vertex}.
    * @param edgerName    The name of the edge to the mother {@link Vertex}.
    * @return             The created {@link Vertex}. */
  private Vertex processGenericRecord(GenericRecord record,
                                      String        name,
                                      String[]      avoids,
                                      boolean       tryDirection,
                                      Vertex        mother,
                                      String        edgeName) {
    Map<String, String> values = getSimpleValues(record, getSimpleFields(record, new String[]{"candid"}));
    Vertex v = vertex(record, name, null);
    if (v == null) {
      return v;
      }
    for (Map.Entry<String, String> entry : values.entrySet()) {
      log.debug("\t" + entry.getKey() + " = " + entry.getValue());
      v.property(entry.getKey(), entry.getValue());
      }
    if (record.get("dec") != null && record.get("ra") != null) {
      v.property("direction", Geoshape.point(new Double(record.get("dec").toString()), new Double(record.get("ra").toString()) - 180));
      }
    _gr.addEdge(mother, v, "has");    
    return v;
    }
    
  /** Process <em>Avro</em> cutout.
    * @param record       The {@link GenericRecord} to process.
    * @param name         The name of cutout.
    * @param mother       The {@link Vertex} to attach to. */
  // TBD: handle binary data
  private void processCutout(GenericRecord record,
                             String        name,
                             Vertex        mother) {
    mother.property(name, record.get("fileName").toString());
    }

  /** Register part of {@link GenericRecord} in <em>HBase</em>.
    * @param record  The {@link GenericRecord} to be registered in <em>HBase</em>.
    * @param fields  The fields to be mapped. */
  private Map<String, String> getSimpleValues(GenericRecord record,
                                              List<String>  fields) {
    Map<String, String> content = new TreeMap<>();
    for (String s : fields) {
      Object o = record.get(s);
      if (o instanceof ByteBuffer) {
        content.put(s, new String(((ByteBuffer)o).array()));
        }
      else {
        content.put(s, o.toString());
        }
      }
    return content;
    }
    
  /** Get {@link Field}s corresponding to simple types
    * and having non-<code>null</code> values.
    * @param record The {@link GenericRecord} to use.
    * @param avoids The array of fields names not to report.
    * @return       The list of coressponding fields. */
  private List<String> getSimpleFields(GenericRecord record,
                                       String[]      avoids) {
    List<String> fields = new ArrayList<>();
    Type type;
    String name;
    boolean veto;
    for (Field field : record.getSchema().getFields()) {
      type = field.schema().getType();
      name = field.name();
      veto = false;
      for (String avoid : avoids) {
        if (name.equals(avoid) || record.get(name) == null) {
          veto = true;
          }
        }
      if (!veto) {
        if (type == Type.BOOLEAN ||
            type == Type.DOUBLE  ||
            type == Type.FLOAT   ||
            type == Type.LONG    ||
            type == Type.INT     ||
            type == Type.UNION   ||
            type == Type.STRING  ||
            type == Type.BYTES) {
            fields.add(name);
          }
        else {
          log.warn("Skipped: " + name + " of " + type);
          }
        }
      }
    return fields;
    }
    
  /** Create or drop a {@link Vertex} according to chosen strategy.
    * @param record    The full {@link GenericRecord}.
    * @param label     The {@link Vertex} label.
    * @param property  The name of {@link Vertex} property.
    *                  If <tt>null</tt> strategy is ignored and {@link Vertex} is created.
    * @return          The created {@link Vertex} or <tt>null</tt>. */
  private Vertex vertex(GenericRecord record,
                        String        label,
                        String        property) {
    Vertex v = null;
    // Not unique Vertex
    if (property == null) {
      if (_drop) {
        return v;
        }
      else {
        log.debug("Creating: " + label);
        return g().addV(label).property("lbl", label).next();
        }
      }
    // Unique Vertex
    if (_drop || _replace) {
      log.debug("Dropping " + label + ": " + property + " = " + record.get(property));
      _gr.drop(label, property, record.get(property), true);
      }
    if (_reuse) {
      log.info("Getting " + label + ": " + property + " = " + record.get(property));
      v = _gr.getOrCreate(label, property, record.get(property));
      }
    if (_create || _replace) {
      log.debug("Creating " + label + ": " + property + " = " + record.get(property));
      v = g().addV(label).property("lbl", label).property(property, record.get(property)).next();
      }
    return v;
    }
    
  /** Give number of created alerts.
    * @return The number of created alerts. */
  public int n() {
    return _n;
    }
    
  private GremlinRecipies _gr;
  
  private int _n = 0;
  
  private int _reportLimit;
  
  private int _commitLimit;
  
  private boolean _create;
  
  private boolean _reuse;
  
  private boolean _replace;
  
  private boolean _drop;
  
  private static String VERSION = "ztf-3.2";
    
  /** Logging . */
  private static Logger log = Logger.getLogger(AvroImporter.class);
                                                
  }
