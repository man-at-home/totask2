/**
 * 
 */
package org.manathome.totask2.util;

/**
 * magic constants.
 * 
 * @author man-at-home
 *
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
}
