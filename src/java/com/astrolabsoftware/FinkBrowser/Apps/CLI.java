package com.astrolabsoftware.FinkBrowser.Apps;

import com.astrolabsoftware.FinkBrowser.Utils.Init;

// Lomikel
import com.Lomikel.Utils.Info;
import com.Lomikel.Utils.StringFile;
import com.Lomikel.Utils.StringResource;
import com.Lomikel.Utils.CommonException;

// Bean Shell
import bsh.Interpreter;
import bsh.EvalError;

// CLI
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

// Java
import java.io.InputStreamReader;

// Log4J
import org.apache.log4j.Logger;

/** Simple Command Line.
  * with usual interval operations.
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
// TBD: put info to screen
// TBD: add Exit button
public class CLI extends com.Lomikel.Apps.CLI {
  
  /** Start {@link Interpreter} and run forever.
    * @param msg The message so show. */
  public CLI(String msg) {
    super(msg);
    }
    
  @Override
  public void setupInterpreter() {
    try {
      interpreter().eval("import com.Lomikel.HBaser.HBaseClient");
      }
    catch (EvalError e) {
      log.error("Cannot load com.Lomikel.HBaser.HBaseClient");
      log.debug("Cannot load com.Lomikel.HBaser.HBaseClient", e);
      }
    super.setupInterpreter();
    }

  /** Start and pass arguments on.
    * @param args The arguments. */
  public static void main(String[] args) {
    Init.init();
    parseArgs(args, "java -jar FinkBrowser.exe.jar");
    new CLI("Welcome to Fink Browser CLI " + Info.release() + "\nhttps://astrolabsoftware.github.io\n");
    }
    
  /** Logging . */
  private static Logger log = Logger.getLogger(CLI.class);
   
 
  }
