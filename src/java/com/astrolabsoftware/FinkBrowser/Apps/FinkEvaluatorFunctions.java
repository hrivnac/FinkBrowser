package com.astrolabsoftware.FinkBrowser.Apps;

// Log4J
import org.apache.log4j.Logger;

/** <code>FinkEvaluatorFuctions</code> provide static functions available to
  * {@link Evaluator}.
  * @opt attributes
  * @opt operations
  * @opt types
  * @opt visibility
  * @author <a href="mailto:Julius.Hrivnac@cern.ch">J.Hrivnac</a> */
public class FinkEvaluatorFunctions {
    
  /** Evaluate, if <tt>ra,dec</tt> are within specified limits.
    * @param ra     The< tt>ra</tt> from the database.
    * @param dec    The< tt>dec</tt> from the database.
    * @param raMin  The minimal value of <tt>ra</tt>.
    * @param raMax  The maximal value of <tt>ra</tt>.
    * @param decMin The minimal value of <tt>dec</tt>.
    * @param decMax The maximal value of <tt>dec</tt>.
  public static boolean isWithinGeoLimits(double ra,
                                          double dec,
                                          double raMin,
                                          double raMax,
                                          double decMin,
                                          double decMax) {
    return ra  > raMin  &&
           ra  < raMax  &&
           dec > decMin &&
           dec < decMax;
    }
      
  /** Logging . */
  private static Logger log = Logger.getLogger(FinkEvaluatorFunctions.class);
                                                
  }
