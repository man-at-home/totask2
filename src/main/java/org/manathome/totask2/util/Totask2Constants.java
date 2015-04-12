/**
 * 
 */
package org.manathome.totask2.util;

/**
 * magic constants.
 * 
 * Environment-Variables are set
 * <ul>
 *   <li>on localhost: as system properties</li>
 *   <li>on heroku: via web ui</li>
 *   <li>on openshift: via command line, e.g. rhc env set TOTASK2_MODE=OPENSHIFT -a TOTASK2</li>
 * </ul>
 * 
 * on openshift: <code>rhc env list -a totask2</code>
 * on heroku:    <code>heroku config          </code>
 * 
 * @author man-at-home
 */
public abstract class Totask2Constants {

    /** set on heroku to write keen.io data. */
    public static final String ENV_KEEN_PROJECT_ID = "KEEN_PROJECT_ID";
    /** set on heroku to write keen.io data. */
    public static final String ENV_KEEN_WRITE_KEY = "KEEN_WRITE_KEY";

    /** used in openshift datadog cartridge. */
    public static final String ENV_OPENSHIFT_DATADOG_IP = "OPENSHIFT_DATADOG_IP";
    /** used in openshift datadog cartridge. */
    public static final String ENV_OPENSHIFT_DATADOG_DOGSTATSD_PORT = "OPENSHIFT_DATADOG_DOGSTATSD_PORT";
    
    /** runtime mode. valid modes at the moment: HEROKU, OPENSHIFT or LOCAL, otherwise "UNKNOWN". */
    public static final String ENV_TOTASK2_MODE = "TOTASK2_MODE";
}
