package com.astrolabsoftware.FinkBrowser.Apps;

// Math3
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

// Java
import static java.lang.Math.PI;

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
    * Usage:
    * <tt>isWithinGeoLimits(raMin, raMax, decMin decMax)</tt>
    * @param ra     The <tt>ra</tt> from the database.
    * @param dec    The <tt>dec</tt> from the database.
    * @param raMin  The minimal value of <tt>ra</tt>.
    * @param raMax  The maximal value of <tt>ra</tt>.
    * @param decMin The minimal value of <tt>dec</tt>.
    * @param decMax The maximal value of <tt>dec</tt>.
    * @return       Whether <tt>ra,dec</tt> from the database are within specified limits. */
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
 
  /** Evaluate, if <tt>ra,dec</tt> are within specified angular from concrete direction.
    * Usage:
    * <tt>isNear(ra0, dec0, delta)</tt>
    * @param ra    The <tt>ra</tt> from the database.
    * @param dec   The <tt>dec</tt> from the database.
    * @param ra0   The central value of <tt>ra</tt> (in deg).
    * @param dec0  The central value of <tt>dec</tt> (in deg).
    * @param delta The maximal angular distance from the central direction (in deg).
    * @return      Whether <tt>ra,dec</tt> from the database are within specified argular. */
  public static boolean isNear(double ra,
                               double dec,
                               double ra0,
                               double dec0,
                               double delta) {
    Vector3D v0 = new Vector3D(ra0 * PI / 180.0, dec0 * PI / 180.0);
    Vector3D v  = new Vector3D(ra  * PI / 180.0, dec  * PI / 180.0);
    double d = Vector3D.angle(v0, v);
    return d <= delta * PI / 180.0;
    }
      
  /** Logging . */
  private static Logger log = Logger.getLogger(FinkEvaluatorFunctions.class);
                                                
  }
