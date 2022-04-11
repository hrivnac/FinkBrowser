package com.astrolabsoftware.FinkBrowser.Apps;

import com.astrolabsoftware.FinkBrowser.Utils.Init;
import com.astrolabsoftware.FinkBrowser.Utils.Info;

// Lomikel
import com.Lomikel.Apps.BSCLI;
import com.Lomikel.Apps.GCLI;
import com.Lomikel.GUI.Icons;
import com.Lomikel.Utils.StringResource;
import com.Lomikel.Utils.LomikelException;

// Bean Shell
import bsh.Interpreter;
import bsh.EvalError;

// CLI
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;

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

  /** Create. 
    * TBD */
  public CLI(String scriptSrc,
             String scriptArgs) {
    super(scriptSrc, scriptArgs);
    }

  /** Start and pass arguments on.
    * @param args The arguments. */
  public static void main(String[] args) {
    Init.init();
    Options options = new Options();
    options.addOption("t", "importStatus",  false, "status of imported alerts");
    options.addOption("i", "objectId",      true, "objectId");
    options.addOption("o", "output-format", true, "output format: txt, csv, json");
    CommandLine cline = parseArgs(args, "java -jar FinkBrowser.exe.jar", options);
    String scriptArgs = null;      
    String scriptSrc = null;
    if (cline.hasOption("importStatus")) {
      scriptArgs = "";
      scriptSrc = "imports.";
      }
    else if (cline.hasOption("objectId")) {
      scriptArgs = "objectId = '" + cline.getOptionValue("objectId") + "';\n";
      scriptSrc = "object.";
      }
    if (_api.equals("bsh") ) {
      log.info("Starting FinkBrowser BeanShell CLI");
      if (scriptSrc != null) {
        scriptSrc += "bsh";
        }
      new BSCLI(Icons.lomikel,
                "<html><h3>http://cern.ch/hrivnac/Activities/Packages/Lomikel</h3></html>",
                "Welcome to Lomikel CLI " + Info.release() + "\nhttp://cern.ch/hrivnac/Activities/Packages/Lomikel\n",
                scriptSrc,
                scriptArgs);
      }
    else if (_api.equals("groovy") ) {
      log.info("Starting FinkBrowser Groovy CLI");
      if (scriptSrc != null) {
        scriptSrc += "groovy";
        }
      new GCLI(scriptSrc,
               scriptArgs);
      }
    else {
      log.fatal("Unknown api language " + _api);
      System.exit(-1);
      }
    }
    
  /** Logging . */
  private static Logger log = Logger.getLogger(CLI.class);
   
 
  }
