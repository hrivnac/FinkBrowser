package com.astrolabsoftware.FinkBrowser.Apps;

import com.astrolabsoftware.FinkBrowser.Utils.Init;
import com.astrolabsoftware.FinkBrowser.Utils.Info;

// Lomikel
import com.Lomikel.GUI.Icons;

// Bean Shell
import bsh.Interpreter;
import bsh.EvalError;

// Swing
import javax.swing.ImageIcon;

// Log4J
import org.apache.log4j.Logger;

/** Simple Command Line.
  * with usual interval operations.
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
public class CLI extends com.Lomikel.Apps.CLI {
  
  /** Start {@link Interpreter} and run forever.
    * @param icon      The {@ImageIcon} for menu.
    * @param toolTiple The menu tooltip.
    * @param msg       The message so show. */
  public CLI(ImageIcon icon,
             String    toolTip,
             String    msg) {
    super(icon, toolTip, msg);
    }
    
  @Override
  public void setupInterpreter() {
    try {
      interpreter().eval("import com.astrolabsoftware.FinkBrowser.HBaser.FinkHBaseClient");
      log.info("FinkHBaseClient imported");
      }
    catch (EvalError e) {
      log.error("Cannot import com.astrolabsoftware.FinkBrowser.HBaser.FinkHBaseClient");
      log.debug("Cannot import com.astrolabsoftware.FinkBrowser.HBaser.FinkHBaseClient", e);
      }
    super.setupInterpreter();
    }

  /** Start and pass arguments on.
    * @param args The arguments. */
  public static void main(String[] args) {
    Init.init();
    parseArgs(args, "java -jar FinkBrowser.exe.jar");
    new CLI(Icons.icon("com/astrolabsoftware/FinkBrowser/Apps/images/Fink.png"),
            "<html><h3>http://134.158.74.221:8080/FinkBrowser</h3></html>",
            "Welcome to Fink Data Explorer " + Info.release() + "\nhttps://cern.ch/hrivnac/Activities/Packages/FinkBrowser\n");
    }
    
  /** Logging . */
  private static Logger log = Logger.getLogger(CLI.class);
   
 
  }
