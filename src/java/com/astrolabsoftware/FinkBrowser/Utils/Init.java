package com.astrolabsoftware.FinkBrowser.Utils;

// Lomikel
import com.Lomikel.Utils.NotifierURL;

// Log4J
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

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
      NotifierURL.notify("", "FinkBrowser", Info.release());
      }
    catch (Exception e) {
      System.err.println(e);
      }
    }
    
  /** Logging . */
  private static Logger log = Logger.getLogger(Init.class);

  }
