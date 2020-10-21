package com.astrolabsoftware.FinkBrowser.WebService;

// Lomikel
import com.Lomikel.Utils.DateTimeManagement;
import com.Lomikel.WebService.HBaseColumnsProcessor;

// Java
import java.util.Map;

// Log4J
import org.apache.log4j.Logger;

/** <code>FinkHBaseColumnsProcessor</code>  extracts X-axes from rows for graphs
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
public class FinkHBaseColumnsProcessor extends HBaseColumnsProcessor {
  
  /** Give the value of the X-axes (=Julian date) corresponding to one table row.
    * @param entry0 One row of the table.
    * @return       The derived value of x-axes. */
  @Override
  public String getX(Map.Entry<String, Map<String, String>> entry0) {
    //return entry0.getKey().split("_")[1];
    return entry0.getValue().get("i:jd");
    }
  
  /** Give the date (from Julian date) corresponding to one table row.
    * @param entry0 One row of the table.
    * @return       The date (may not correspond to the row timestamp). */
  @Override
  public String getXDate(Map.Entry<String, Map<String, String>> entry0) {
    String days = getX(entry0);
    return DateTimeManagement.julianDate2String(Double.valueOf(days), "yyyy MM dd HH:mm:ss.nnnnnnnnn");
    }

  /** Logging . */
  private static Logger log = Logger.getLogger(FinkHBaseColumnsProcessor.class);

  }
