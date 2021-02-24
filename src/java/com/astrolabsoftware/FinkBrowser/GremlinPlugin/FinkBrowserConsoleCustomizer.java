package com.astrolabsoftware.FinkBrowser.GremlinPlugin;

// TinkerPop
import org.apache.tinkerpop.gremlin.jsr223.console.ConsoleCustomizer;
import org.apache.tinkerpop.gremlin.jsr223.console.GremlinShellEnvironment;
import org.apache.tinkerpop.gremlin.jsr223.console.RemoteAcceptor;

// Log4J
import org.apache.log4j.Logger;

/** Allows remote access to {@link FinkBrowserConnector}.
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
public class FinkBrowserConsoleCustomizer implements ConsoleCustomizer {

  @Override
  public RemoteAcceptor	 getRemoteAcceptor(GremlinShellEnvironment environment) {
    return new FinkBrowserRemoteAcceptor();
    }
  
  /** Logging . */
  private static Logger log = Logger.getLogger(FinkBrowserConsoleCustomizer.class);

  }
