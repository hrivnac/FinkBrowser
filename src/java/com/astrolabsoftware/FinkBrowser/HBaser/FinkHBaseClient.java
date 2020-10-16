package com.astrolabsoftware.FinkBrowser.HBaser;
// Lomikel
import com.Lomikel.HBaser.HBaseClient;

// Java
import java.io.IOException;

// Log4J
import org.apache.log4j.Logger;

/** <code>FinkHBaseClient</code> handles connectionto HBase table
  * with specific Fink functionality. 
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
public class FinkHBaseClient extends HBaseClient {
   
 /** Create.
   * @param zookeepers The coma-separated list of zookeper ids.
   * @param clientPort The client port. 
   * @throws IOException If anything goes wrong. */
 public FinkHBaseClient(String zookeepers,
                        String clientPort) throws IOException {
   super(zookeepers, clientPort);
   }
       
 /** Create.
   * @param zookeepers The coma-separated list of zookeper ids.
   * @param clientPort The client port. 
   * @throws IOException If anything goes wrong. */
 public FinkHBaseClient(String zookeepers,
                        int    clientPort) throws IOException {
   super(zookeepers, clientPort);
   }
   
 /** Create.
   * @param url The HBase url.
   * @throws IOException If anything goes wrong. */
 public FinkHBaseClient(String url) throws IOException {
   super(url);
   }
   
 /** Create on <em>localhost</em>.
   * @throws IOException If anything goes wrong. */
 public FinkHBaseClient() throws IOException {
   super();
   }
  
  /** Logging . */
  private static Logger log = Logger.getLogger(FinkHBaseClient.class);

  }
