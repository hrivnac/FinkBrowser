package com.astrolabsoftware.FinkBrowser.WebService;

// JHTools
import com.JHTools.Utils.DateTimeManagement;
import com.JHTools.WebService.HBaseColumnsProcessor;

// Java
import java.util.Map;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

// Log4J
import org.apache.log4j.Logger;

/** <code>FinkHBaseColumnsProcessor</code>  extracts X-axes from rows for graphs
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
public class FinkHBaseColumnsProcessor implements HBaseColumnsProcessor {
  
  @Override
  public String getX(Map.Entry<String, Map<String, String>> entry0) {
    //return entry0.getKey().split("_")[1];
    return entry0.getValue().get("i:jd");
    }
  
  @Override
  public String getXDate(Map.Entry<String, Map<String, String>> entry0) {
    String days = getX(entry0);
    return DateTimeManagement.julianDate2String(Double.valueOf(days));
    }

  /** Logging . */
  private static Logger log = Logger.getLogger(HBaseColumnsProcessor.class);

  }
