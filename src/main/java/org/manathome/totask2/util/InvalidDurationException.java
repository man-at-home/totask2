/**
 * 
 */
package org.manathome.totask2.util;

/**
 * duration conversion problems..
 * 
 * @see    DurationConverter
 * @author man-at-home
 *
 */
public class InvalidDurationException extends ToTaskExceptionBase {

    private static final long serialVersionUID = -8513905419915738581L;

    /**
     * default ctor.
     */
    public InvalidDurationException() {}

    /**
     * @param message
     */
    public InvalidDurationException(String message) {
        super(message);
    }

}
