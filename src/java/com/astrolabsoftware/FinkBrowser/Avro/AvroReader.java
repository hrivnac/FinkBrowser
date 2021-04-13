package com.astrolabsoftware.FinkBrowser.Avro;

import com.astrolabsoftware.FinkBrowser.Utils.Init;

// Lomikel
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
import ztf.alert.candidate;

// Log4J
import org.apache.log4j.Logger;

/** <code>AvroReader</code> imports <em>Avro</em> files into <em>JanusGraph</em>.
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
// TBD: allow reset
// TBD: allow getOrCreate
public class AvroReader extends JanusClient {
        
  /** Import Avro files or directory. 
    * @param args[0] The Janusgraph properties file. 
    * @param args[1] The Avro file or directory with Avro files.
    * @param args[2] The number of events to commit in one step (-1 means commit only at the end).
    * @throws LomikelException If anything goes wrong. */
   public static void main(String[] args) throws IOException {
    Init.init();
    if (args.length != 3) {
      log.error("AvroReader.exe.jar <JanusGraph properties> [<file>|<directory>] <commit limit>");
      System.exit(-1);
      }
    try {
      AvroReader reader = new AvroReader(            args[0],
                                         new Integer(args[2]));
      reader.timerStart();
      reader.process(            args[1]);
      reader.close();
      }
    catch (LomikelException e) {
      log.fatal("Cannot import " + args[1] + " into " + args[0], e);
      System.exit(-1);
      }
    }
  
  /** Create with JanusGraph properties file.
    * @param properties  The file with the complete Janusgraph properties.
    * @param commitLimit The number of events to commit in one step (-1 means commit only at the end). */
  public AvroReader(String properties,
                    int    commitLimit) {
    super(properties);
    _commitLimit = commitLimit;
    _gr = new GremlinRecipies(this);
    }
        
  /** Process directory with <em>Avro</em> alert files.
     * @param dirFN The dirname of directiory with data file. */
  public void processDir(String dirFN) {  
    log.info("Loading directory " + dirFN);
    File dir = new File(dirFN);
    int i = 0;
    for (String dataFN : dir.list()) {
      if (dataFN.endsWith(".avro")) {
        try {
          process(dirFN + "/" + dataFN);
          i++;
          }
        catch (IOException | LomikelException e) {
          log.error("Failed to process " + dirFN + "/" + dataFN, e);
          }
        }
      else {
        log.warn("Not Avro file: " + dataFN);
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
  // TBD: use generated schema (problem: it mixes ztf.alert namespace and class
  public void process(String fn) throws IOException, LomikelException {
    log.debug("Loading " + fn);
    File file = new File(fn);
    if (file.isDirectory()) {
      processDir(fn);
      return;
      }
    else if (!file.isFile()) {
      log.error("Not a file/directory: " + fn);
      return;
      }
    /*
    DatumReader<candidate> candidateDatumReader = new SpecificDatumReader<candidate>(candidate.class);
    DataFileReader<candidate> dataFileReader = new DataFileReader<candidate>(file, candidateDatumReader);
    candidate c = null;
    while (dataFileReader.hasNext()) {
      c = dataFileReader.next(c);
      log.info(c);
      }*/
    DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>();
    DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader);
    GenericRecord record = null;
    int n = 1;
    while (dataFileReader.hasNext()) {
      record = dataFileReader.next(record);
      processAlert(record);
      }
    } 
   
  /** Process <em>Avro</em> alert.
    * @param record The full alert {@link GenericRecord}. */
  // TBD: allow getOrCreate
  private Vertex processAlert(GenericRecord record) {
    Map<String, String> values = getSimpleValues(record, getSimpleFields(record, new String[]{}));
    log.debug("alert:"); 
    Vertex v = g().addV("alert").property("lbl", "alert").next();
    Vertex vv;
    for (Map.Entry<String, String> entry : values.entrySet()) {
      log.debug("\t" + entry.getKey() + " = " + entry.getValue());
      v.property(entry.getKey(), entry.getValue());
      }
    vv = processCandidate((GenericRecord)(record.get("candidate")));
    GremlinRecipies gr = new GremlinRecipies(this);
    gr.addEdge(v, vv, "has");
    Array prv_candidates = (Array)(record.get("prv_candidates"));
    int n = 0;
    for (Object o : prv_candidates) {
      vv = processPrvCandidate((GenericRecord)o);
      gr.addEdge(v, vv, "has");
      }
    for (String s : new String[]{"Science", "Template", "Difference"}) { 
      vv = processCutout((GenericRecord)(record.get("cutout" + s)));
      gr.addEdge(v, vv, s);
      }
    timer("alerts created", ++_n, 100, _commitLimit);      
    return v;
    }
  
  /** Process <em>Avro</em> candidate.
    * @param record The {@link GenericRecord} with <em>candidate</em>. */
  private Vertex processCandidate(GenericRecord record) {
    Map<String, String> values = getSimpleValues(record, getSimpleFields(record, new String[]{}));
    String rowkey = record.get("candid").toString();
    log.debug("candidate:"); 
    Vertex v = g().addV("candidate").property("lbl", "candidate").next();
    for (Map.Entry<String, String> entry : values.entrySet()) {
      log.debug("\t" + entry.getKey() + " = " + entry.getValue());
      v.property(entry.getKey(), entry.getValue());
      }
    v.property("direction", Geoshape.point(new Double(record.get("dec").toString()), new Double(record.get("ra").toString()) - 180));
    return v;
    }
    
  /** Process <em>Avro</em> prv_candidate.
    * @param record The {@link GenericRecord} with <em>prv_candidate</em>. */
  // TBD: refactor with processCandidate
  private Vertex processPrvCandidate(GenericRecord record) {
    Map<String, String> values = getSimpleValues(record, getSimpleFields(record, new String[]{}));
    log.debug("prv_candidate:"); 
    Vertex v = g().addV("prv_candidate").property("lbl", "prv_candidate").next();
    for (Map.Entry<String, String> entry : values.entrySet()) {
      log.debug("\t" + entry.getKey() + " = " + entry.getValue());
      v.property(entry.getKey(), entry.getValue());
      }
    //v.property("direction", Geoshape.point(new Double(record.get("dec").toString()), new Double(record.get("ra").toString()) - 180));
    return v;
    }
    
  /** Process <em>Avro</em> cutoutScience.
    * @param record The {@link GenericRecord} with <em>cutoutScience</em>. */
  // TBD: handle binary data
  private Vertex processCutout(GenericRecord record) {
    Map<String, String> values = getSimpleValues(record, getSimpleFields(record, new String[]{}));
    log.debug("cutout:"); 
    log.debug("\tfileName = " + record.get("fileName").toString());
    Vertex v = g().addV("cutout").property("lbl", "cutout").next();
    for (Map.Entry<String, String> entry : values.entrySet()) {
      v.property(entry.getKey(), entry.getValue());
      }
    return v;
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
        content.put(s, new String(((ByteBuffer)o).array())); // TBD: handle better
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
      if (type == Type.BOOLEAN ||
          type == Type.DOUBLE  ||
          type == Type.FLOAT   ||
          type == Type.LONG    ||
          type == Type.INT     ||
          type == Type.STRING) {
        veto = false;
        for (String avoid : avoids) {
          if (name.equals(avoid) || record.get(name) == null) {
            veto = true;
            }
          }
        if (!veto) {
          fields.add(name);
          }
        }
      }
    return fields;
    }
    
  private GremlinRecipies _gr;
  
  private int _n = 0;
  
  private int _commitLimit;
    
  /** Logging . */
  private static Logger log = Logger.getLogger(AvroReader.class);
                                                
  }
