package com.astrolabsoftware.FinkBrowser.Utils;

// Lomikel
import com.Lomikel.Utils.NotifierURL;
import com.Lomikel.HBaser.Evaluator;

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

  /** Setup system. */
  public static void init() {
    init(false, false);
    }

  /** Setup system for Web Service. */
  public static void initWS() {
    init(true, false);
    }

  /** Setup Logging system.
    * @param quiet If no outupt is required. */
  public static void init(boolean ws,
                          boolean quiet) {
    if (_initialised) {
      log.debug("Already initialised");
      return;
      }
    try {
      if (ws) {
        Evaluator.setAuxFuctions("com.astrolabsoftware.FinkBrowser.HBaser.FinkEvaluatorFunctions",
                                 "com/astrolabsoftware/FinkBrowser/HBaser/FinkEvaluatorFunctions.bsh"); 
        NotifierURL.notify("", "FinkBrowserWS", Info.release());
        }
      else {
        PropertyConfigurator.configure(Init.class.getClassLoader().getResource("com/astrolabsoftware/FinkBrowser/Utils/log4j.properties"));
        Evaluator.setAuxFuctions("com.astrolabsoftware.FinkBrowser.HBaser.FinkEvaluatorFunctions",
                                 "com/astrolabsoftware/FinkBrowser/HBaser/FinkEvaluatorFunctions.bsh"); 
        NotifierURL.notify("", "FinkBrowser", Info.release());
        }
      }
    catch (Exception e) {
      System.err.println(e);
      }
    _initialised = true;
    }
    
  public static boolean _initialised = false;  
    
  /** Logging . */
  private static Logger log = Logger.getLogger(Init.class);

  }
