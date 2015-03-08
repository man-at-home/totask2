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
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public InvalidDurationException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidDurationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public InvalidDurationException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public InvalidDurationException(Throwable cause) {
        super(cause);
    }

}
