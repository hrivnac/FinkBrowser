package com.astrolabsoftware.FinkBrowser.Utils;

// Log4J
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;

// Java
import java.util.Enumeration;

/** <code>Init</code> provides common initialisation.
  * <img src="doc-files/logo.png"/>
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
public class Init {

  /** Setup Logging system. */
  public static void init() {
    init(false);
    }

  /** Setup Logging system.
    * @param quiet If no outupt is required. */
  public static void init(boolean quiet) {
    try {
      PropertyConfigurator.configure(Init.class.getClassLoader().getResource("com/astrolabsoftware/FinkBrowser/Utils/log4j.properties"));
      fixLog4J();
      if (!quiet) {
        log.info("Initialised, version: " + Info.release());
        log.info(Info.configuration());
        }
      }
    catch (Exception e) {
      System.err.println(e);
      }
    }
    
  /** Customise Log4J {@link Logger}s. */
  private static void fixLog4J() {
    for (String s : WARN) {
      Logger.getLogger(s).setLevel(Level.WARN);
      }
    for (String s : ERROR) {
      Logger.getLogger(s).setLevel(Level.ERROR);
      }
    for (String s : DEBUG) {
      Logger.getLogger(s).setLevel(Level.DEBUG);
      }
    //Enumeration<Logger> e = LogManager.getCurrentLoggers();
    //while (e.hasMoreElements()) {
    //  e.nextElement().setLevel(Level.WARN); 
    //  }
    }
          
  private static String[] DEBUG = {"org.janusgraph.graphdb.transaction.StandardJanusGraphTx"};
          
  private static String[] WARN = {};
          
  private static String[] ERROR = {"org.apache.hadoop.hbase.HBaseConfiguration"};
    
  /** Logging . */
  private static Logger log = Logger.getLogger(Init.class);

  }
