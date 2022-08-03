package com.astrolabsoftware.FinkBrowser.Avro;

import com.astrolabsoftware.FinkBrowser.Utils.Init;

// Lomikel
import com.Lomikel.Januser.JanusClient;
import com.Lomikel.Januser.GremlinRecipies;
import com.Lomikel.Utils.LomikelException;

// Avro
import org.apache.avro.Schema.Field;
import org.apache.avro.Schema.Type;
import org.apache.avro.io.DatumReader;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericData.Array;
import org.apache.avro.generic.GenericDatumReader;

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
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Base64;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.FileSystems;

// Log4J
import org.apache.log4j.Logger;

/** <code>AvroImporter</code> imports <em>Avro</em> files into <em>JanusGraph</em>.
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
public class AvroImporter extends JanusClient {
        
  /** Import Avro files or directory. 
    * @param args[0] The Janusgraph properties file. 
    * @param args[1] The Avro file or directory with Avro files.
    * @param args[2] The directory for FITS files. If <tt>null</tt> or empty, FITS are included in the Graph.
    * @param args[3] The number of events to use for progress report (-1 means no report untill the end).
    * @param args[4] The number of events to commit in one step (-1 means commit only at the end).
    * @param args[5] The creation strategy. <tt>create,drop,replace,skip</tt>.
    * @throws LomikelException If anything goes wrong. */
   public static void main(String[] args) throws IOException {
    Init.init();
    if (args.length != 6) {
      log.error("AvroImporter.exe.jar <JanusGraph properties> [<file>|<directory>] <report limit> <commit limit> [create|reuse|drop]");
      System.exit(-1);
      }
    try {
      AvroImporter importer = new AvroImporter(            args[0],
                                               new Integer(args[3]),
                                               new Integer(args[4]),
                                                           args[5],
                                                           args[2]);
      importer.timerStart();                    
      importer.process(args[1]);
      if (!importer.skip()) {
        importer.commit();
        }
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
    * @param strategy    The creation strategy. <tt>drop,replace,getOrCreate</tt>.
    * @param fitsDir     The directory for FITS files. If <tt>null</tt> or empty, FITS are included in the Graph. */
  public AvroImporter(String properties,
                      int    reportLimit,
                      int    commitLimit,
                      String strategy,
                      String fitsDir) {
    super(properties);
    if (fitsDir != null && fitsDir.trim().equals("")) {
      fitsDir = null;
      }
    log.info("Reporting after each " + reportLimit + " alerts");
    log.info("Committing after each " + commitLimit + " alerts");
    log.info("Using strategy: " + strategy);
    if (fitsDir == null) {
      log.info("Writing FITS into Graph");
      }
    else {
      log.info("Writing FITS into: " + fitsDir);
      }
    log.info("Importing at " + _date);
    _reportLimit = reportLimit;
    _commitLimit = commitLimit;
    _fitsDir     = fitsDir;
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
    if (strategy.contains("skip")) {
      _skip = true;
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
    register(fn);
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
    
  /** Register <em>Import</em> {@link Vertex}.
     * @param fn The filename of the data file
     *           or directory with files. */
  public void register(String fn) {
    if (_top) {
      _topFn = fn;
      now();
      log.info(g().V().limit(1).next());
      Vertex import1 = g().addV("Import").property("lbl", "Import").property("importSource", fn).property("importDate", _date).next();
      Vertex imports = g().V().has("lbl", "site").has("title", "IJCLab").out().has("lbl", "Imports").next();
      _gr.addEdge(imports, import1, "has"); 
      commit();
      }
    _top = false;
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
    _nAlerts++;
    Map<String, String> values = getSimpleValues(record, getSimpleFields(record, new String[]{"objectId",
                                                                                              "candidate",
                                                                                              "prv_candidates",
                                                                                              "cutoutScience",
                                                                                              "cutoutTemplate",
                                                                                              "cutoutDifference"}));
    log.debug("alert:"); 
    Vertex v = vertex(record, "alert", "objectId");
    if (v != null) {
      for (Map.Entry<String, String> entry : values.entrySet()) {
        log.debug("\t" + entry.getKey() + " = " + entry.getValue());
        try {
          v.property(entry.getKey(), entry.getValue());
          }
        catch (IllegalArgumentException e) {
          log.error("Cannot add property: " + entry.getKey() + " = " + entry.getValue(), e);
          }
        }
      v.property("alertVersion", VERSION);
      v.property("importDate",   _date);
      }
    String ss;
    processGenericRecord((GenericRecord)(record.get("candidate")),
                         "candidate",
                         "candid",
                         true,
                         v,
                         "has",
                         null);
    Vertex vv = vertex(record, "prv_candidates", null);
    _gr.addEdge(v, vv, "has");    
    Array a = (Array)record.get("prv_candidates");
    if (a != null) {
      for (Object o : a) {
        _nPrvCandidates++;
        processGenericRecord((GenericRecord)o,
                             "prv_candidate",
                             "candid",
                             true,
                             vv,
                             "holds",
                             null);
        } 
      }
    processCutout(record, v);
    timer("alerts processed", ++_n, _reportLimit, _commitLimit); 
    return v;
    }
   
  /** Process <em>Avro</em> {@link GenericRecord}.
    * @param record       The {@link GenericRecord} to process.
    * @param name         The name of new {@link Vertex}.
    * @param idName       The name of the unique identifying field.
    * @param tryDirection Whether try created <em>Direction</em> property from <em>ra,dec</em> fields.
    * @param mother       The mother {@link Vertex}.
    * @param edgerName    The name of the edge to the mother {@link Vertex}.
    * @param fields       The array of fields to fill. All fields are filled if <code>null</code>
    * @return             The created {@link Vertex}. */
  private Vertex processGenericRecord(GenericRecord record,
                                      String        name,
                                      String        idName,
                                      boolean       tryDirection,
                                      Vertex        mother,
                                      String        edgeName,
                                      String[]      fields) {
    String[] idNameA;
    if (idName == null) {
      idNameA= new String[]{};
      }
    else {
      idNameA = new String[]{idName};
      }
    Map<String, String> values = getSimpleValues(record, getSimpleFields(record, idNameA));
    Vertex v = vertex(record, name, idName);
    if (v == null) {
      return v;
      }
    if (fields != null) {
      for (String field : fields) {
        if (values.containsKey(field)) {
          //log.debug("\t" + field + " = " + values.get(field));
          v.property(field, values.get(field));
          }
        }
      }
    else {
      for (Map.Entry<String, String> entry : values.entrySet()) {
        //log.debug("\t" + entry.getKey() + " = " + entry.getValue());
        v.property(entry.getKey(), entry.getValue());
        }
      }
    if (record.get("dec") != null && record.get("ra") != null) {
      v.property("direction", Geoshape.point(new Double(record.get("dec").toString()), new Double(record.get("ra").toString()) - 180));
      }
    _gr.addEdge(mother, v, "has");    
    return v;
    }
    
  /** Process <em>Avro</em> cutout.
    * @param record       The {@link GenericRecord} to process.
    * @param mother       The {@link Vertex} to attach to. */
  private void processCutout(GenericRecord record,
                             Vertex        mother) {
    Vertex v = vertex(record, "cutout", null);
    GenericRecord r;
    String fn;
    byte[] data;
    for (String s : new String[]{"Science", "Template", "Difference"}) { 
      r = (GenericRecord)(record.get("cutout" + s));
      fn = r.get("fileName").toString();
      data = ((ByteBuffer)(r.get("stampData"))).array();
      if (fitsDir() == null) {
        v.property("cutout" + s + "Fn", fn);
        v.property("cutout" + s,        Base64.getEncoder().encodeToString(data));
        }
      else {
        v.property("cutout" + s + "Fn", "file:" + fitsDir() + "/" + fn);
        writeFits(fn, data);
        }
      }
    _gr.addEdge(mother, v, "has");
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
      else if (o != null) {
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
    // Do nothing
    if (_skip) {
      return v;
      }
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
      v = _gr.getOrCreate(label, property, record.get(property)).get(0);
      }
    if (_create || _replace) {
      log.debug("Creating " + label + ": " + property + " = " + record.get(property));
      v = g().addV(label).property("lbl", label).property(property, record.get(property)).next();
      }
    return v;
    }
        
  /** Write FITS file.
    * @param fn   The FITS file name.
    * @param data The FITS file content. */
  protected void writeFits(String fn,
                           byte[] data) {
    try {
      Files.write(FileSystems.getDefault().getPath(fitsDir() + "/" + fn), data);
      }
    catch (IOException e) {
      log.error("Cannot write " + fn, e);
      }
    }
    
  /** The directory for FITS files.
    * @return The FITS file directory. */
  protected String fitsDir() {
    return _fitsDir;
    } 
    
  /** Give number of created alerts.
    * @return The number of created alerts. */
  protected int n() {
    return _n;
    }
    
  /** Tell, whether import shpuld be skipped.
    * @return Whether import shpuld be skipped. */
  protected boolean skip() {
    return _skip;
    }
    
  @Override
  public void close() {    
    g().V().has("lbl", "Import").has("importSource", _topFn).has("importDate", _date).property("complete", true).property("nAlerts", _nAlerts).next();
    commit();
    log.info("Import statistics:");
    log.info("\talerts:         " + _nAlerts);
    log.info("\tprv_candidates: " + _nPrvCandidates);
    log.info("Imported at " + _date);
    super.close();
    }
    
  /** Set new {@link Date}. */
  protected void now() {
    _date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()).toString();
    }
   
  private GremlinRecipies _gr;
  
  private int _n = 0;
  
  private int _reportLimit;
  
  private int _commitLimit;
  
  private String _fitsDir;
  
  private boolean _create;
  
  private boolean _reuse;
  
  private boolean _replace;
  
  private boolean _drop;
  
  private boolean _skip;
  
  private int _nAlerts = 0;
  
  private int _nPrvCandidates = 0;
  
  private String _date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()).toString();
  
  private boolean _top = true;
  
  private String _topFn;
     
  private static String VERSION = "ztf-3.2";
    
  /** Logging . */
  private static Logger log = Logger.getLogger(AvroImporter.class);
                                                
  }
