package com.astrolabsoftware.FinkBrowser.WebService;

// Lomikel
import com.Lomikel.Utils.DateTimeManagement;
import com.Lomikel.WebService.PropertiesProcessor;

// Java
import java.util.Map;

// Log4J
import org.apache.log4j.Logger;

/** <code>FinkPropertiesProcessor</code> extracts X-axes from rows for graphs.
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
public class FinkPropertiesProcessor extends PropertiesProcessor {

  @Override
  public String getTimestamp(String entry) {
    String date = getDate(entry);
    return Long.toString(DateTimeManagement.string2time(date, "yyyy MM dd HH:mm:ss.SSS"));
    }
  
  @Override
  public String getDate(String jd) {
    return DateTimeManagement.julianDate2String(Double.valueOf(jd), "yyyy MM dd HH:mm:ss.SSS");
    }

  @Override
  // TBD
  public String ra() {
    return "i:ra";
    }
    
  @Override
  // TBD
  public String dec() {
    return "i:dec";
    }

  /** Logging . */
  private static Logger log = Logger.getLogger(PropertiesProcessor.class);

  }
