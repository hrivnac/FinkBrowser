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
  * Implememts:
  * https://fink-portal.org/api
  * https://github.com/astrolabsoftware/fink-notebook-template
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
public class FUC {

  /** Create. */
  public FUC() {
    }

  /** Start and pass arguments on.
    * @param args The arguments. */
  public static void main(String[] args) {
    System.out.println(doit(args));
    }
    
  /** Start and pass arguments on.
    * @param args The arguments. */
  public static String doit(String[] args) {
    Init.init();
    Options options = new Options();
    options.addOption("r", "gremlin",       true,  "gremlin graph search clause");
    options.addOption("t", "importStatus",  false, "status of imported alerts");
    options.addOption("i", "objectId",      true,  "objectId");
    options.addOption("o", "output-format", true,  "output format: txt (default), json");
    CommandLine cline = CLI.parseArgs(args, "java -jar FinkBrowser.exe.jar", options);
    String scriptArgs = "";      
    String scriptSrc = null;
    if (cline.hasOption("output-format")) {
      scriptArgs += "oformat = '" + cline.getOptionValue("output-format") + "';\n";
      }
    else {
      scriptArgs += "oformat = 'txt';\n";
      }
    if (cline.hasOption("gremlin")) {
      scriptArgs += "gremlin = '" + cline.getOptionValue("gremlin") + "';\n";
      scriptSrc = "gremlin.";
      }
    else if (cline.hasOption("importStatus")) {
      scriptArgs += ";\n";
      scriptSrc = "imports.";
      }
    else if (cline.hasOption("objectId")) {
      scriptArgs += "objectId = '" + cline.getOptionValue("objectId") + "';\n";
      scriptSrc = "object.";
      }
    if (CLI.api().equals("bsh") ) {
      log.info("Starting Fink Universal Client in BeanShell");
      if (scriptSrc != null) {
        scriptSrc += "bsh";
        }
      _cli = new BSCLI(Icons.lomikel, // TBD: better icon
                       "<html><h3>http://cern.ch/hrivnac/Activities/Packages/FinkBrowser</h3></html>",
                       "Welcome to Fink Universal Client " + Info.release() + "\nhttp://cern.ch/hrivnac/Activities/Packages/FinkBrowser\n" + CLI.help(),
                       scriptSrc,
                       scriptArgs);
      }
    else if (CLI.api().equals("groovy") ) {
      log.info("Starting Fink Universal Client in Groovy");
      if (scriptSrc != null) {
        scriptSrc += "groovy";
        }
      _cli = new GCLI(scriptSrc,
                      scriptArgs);
      }
    else {
      log.fatal("Unknown api language " + CLI.api());
      return "FATAL: Unknown api language " + CLI.api();
      }
    return _cli.execute();
    }

  /** Give the embedded {@link CLI}.
    * @return The embedded {@link CLI}. */
  public static CLI cli() {
    return _cli;
    }
    
  private static CLI _cli;
    
  /** Logging . */
  private static Logger log = Logger.getLogger(FUC.class);
   
 
  }
