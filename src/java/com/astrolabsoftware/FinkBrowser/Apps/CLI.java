package com.astrolabsoftware.FinkBrowser.Apps;

import com.astrolabsoftware.FinkBrowser.Utils.Init;

// JHTools
import com.JHTools.Utils.Info;
import com.JHTools.Utils.StringFile;
import com.JHTools.Utils.StringResource;
import com.JHTools.Utils.CommonException;

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
public class CLI extends com.JHTools.Apps.CLI {

  /** TBD */
  public static void main(String[] args) {
    Init.init();
    parseArgs(args);
    new CLI();
    }
  
  /** Start {@link Interpreter} and run forever. */
  public CLI() {
    super();
    interpreter().print("Welcome to Fink Browser CLI " + Info.release() + "\n");
    interpreter().print("https://astrolabsoftware.github.io\n");
    try {
      interpreter().eval("import com.JHTools.HBaser.HBaseClient");
      }
    catch (EvalError e) {
      log.error("Cannot load  com.JHTools.HBaser.HBaseClient");
      log.debug("Cannot load  com.JHTools.HBaser.HBaseClient", e);
      }
    }
    
  /** Parse the cli arguments.
    * @param args The cli arguments. */
  private static void parseArgs(String[] args) {
    CommandLineParser parser = new BasicParser();
    Options options = new Options();
    options.addOption("h", "help",    false, "show help");
    options.addOption("q", "quiet",   false, "minimal direct feedback");
    options.addOption(OptionBuilder.withLongOpt("source")
                                   .withDescription("source bsh file (init.bsh is also read)")
                                   .hasArg()
                                   .withArgName("file")
                                   .create("s"));
    try {
      CommandLine line = parser.parse(options, args );
      if (line.hasOption("help")) {
        new HelpFormatter().printHelp("java -jar FinkBrowser.exe.jar", options);
        System.exit(0);
        }
      if (line.hasOption("quiet")) {
        _quiet = true;
        }
      if (line.hasOption("source")) {
        _source = line.getOptionValue("source");
        }
      }
    catch (ParseException e) {
      new HelpFormatter().printHelp("java -jar AstroLabNet.exe.jar", options);
      System.exit(-1);
      }
    }
  
  private static boolean _quiet = false;
  
  private static String _source = null;
    
  /** Logging . */
  private static Logger log = Logger.getLogger(CLI.class);
   
 
  }
