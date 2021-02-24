package com.astrolabsoftware.FinkBrowser.GremlinPlugin;

import com.astrolabsoftware.FinkBrowser.Januser.Alert;

// Lomikel
import com.Lomikel.HBaser.HBaseClient;
import com.Lomikel.Phoenixer.PhoenixProxyClient;

// TinkerPop
import org.apache.tinkerpop.gremlin.jsr223.AbstractGremlinPlugin;
import org.apache.tinkerpop.gremlin.jsr223.DefaultImportCustomizer;
import org.apache.tinkerpop.gremlin.jsr223.DefaultImportCustomizer.Builder;
import org.apache.tinkerpop.gremlin.jsr223.ImportCustomizer;
import org.apache.tinkerpop.gremlin.jsr223.console.ConsoleCustomizer;

// Log4J
import org.apache.log4j.Logger;

/** Add connection to aux databases into Gremlin.
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
// TBD: refactor with Atlascope
// TBD: parametrise
public class FinkBrowserConnector extends AbstractGremlinPlugin {

  /** Create. */
  public FinkBrowserConnector() {
    super("finkbrowser.connector",
          imports(new Class[]{Alert.class}),
          new FinkBrowserConsoleCustomizer());
    }

  /** Give the instance of itself.
    * @return The {@link FinkBrowserConnector} instance. */
  public static FinkBrowserConnector instance() {
    return new FinkBrowserConnector();
    }
  
  /** Create {@link ImportCustomizer}.
    * @param classes The {@link Class}es to add.
    * @return The created  {@link ImportCustomizer}. */
  // TBD: add methods
  private static final ImportCustomizer imports(Class[] classes) {
    Builder builder = DefaultImportCustomizer.build();
    for (Class cl : classes) {
      builder.addClassImports(cl);
      }
    return builder.create();
    }

  /** Logging . */
  private static Logger log = Logger.getLogger(FinkBrowserConnector.class);

  }
