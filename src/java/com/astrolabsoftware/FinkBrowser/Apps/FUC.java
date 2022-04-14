package com.astrolabsoftware.FinkBrowser.Apps;

import com.astrolabsoftware.FinkBrowser.Utils.Init;
import com.astrolabsoftware.FinkBrowser.Utils.Info;

// Lomikel
import com.Lomikel.Apps.CLI;
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
public class FUC {

  /** Create. 
    * TBD */
  public FUC() {
    }

  /** Start and pass arguments on.
    * @param args The arguments. */
  public static void main(String[] args) {
    System.out.println(doit(args));
    System.exit(0);
    }
    
  /** TBD
    * @param args The arguments. */
  public static String doit(String[] args) {
    Init.init();
    Options options = new Options();
    options.addOption("t", "importStatus",  false, "status of imported alerts");
    options.addOption("i", "objectId",      true, "objectId");
    options.addOption("o", "output-format", true, "output format: txt, csv, json");
    CommandLine cline = CLI.parseArgs(args, "java -jar FinkBrowser.exe.jar", options);
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
    CLI cli = null;
    if (CLI.api().equals("bsh") ) {
      log.info("Starting Fink Universal Client in BeanShell");
      if (scriptSrc != null) {
        scriptSrc += "bsh";
        }
      cli = new BSCLI(Icons.lomikel, // TBD
                      "<html><h3>http://cern.ch/hrivnac/Activities/Packages/FinkBrowser</h3></html>",
                      "Welcome to Fink Universal Client " + Info.release() + "\nhttp://cern.ch/hrivnac/Activities/Packages/FinkBrowser\n",
                      scriptSrc,
                      scriptArgs);
      }
    else if (CLI.api().equals("groovy") ) {
      log.info("Starting Fink Universal Client in Groovy");
      if (scriptSrc != null) {
        scriptSrc += "groovy";
        }
      cli = new GCLI(scriptSrc,
                     scriptArgs);
      }
    else {
      log.fatal("Unknown api language " + CLI.api());
      System.exit(-1);
      }
    return cli.execute();
    }
    
  /** Logging . */
  private static Logger log = Logger.getLogger(FUC.class);
   
 
  }
