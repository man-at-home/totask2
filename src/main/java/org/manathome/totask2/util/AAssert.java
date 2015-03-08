package org.manathome.totask2.util;

import javax.validation.constraints.NotNull;

/** checking arguments (convenience class). */
public class AAssert {

    private AAssert() {
    }
    
    
    /** argument checking.
     *  
     * @throws NullPointerException
     * */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /** argument checking.
    *  
    * @throws NullPointerException
    */
    public static <T> T checkNotNull(T reference, @NotNull String msg) {
        if (reference == null) {
            throw new NullPointerException(msg);
        }
        return reference;
    }
    
    /**
     * check.
     * 
     * @throws NullPointerException if null
     * @throws AssertionError if empty
     * 
     * @param nonEmptyString (may not be null or "")
     * @param msg error message
     * @return nonEmptyString unchanged
     */
    public static String checkNotNullOrEmpty(final String nonEmptyString, @NotNull final String msg) {
        // assert nonEmptyString != null && nonEmptyString.trim().length() > 0;
        if (checkNotNull(nonEmptyString, msg).trim().length() == 0) {
            throw new AssertionError(msg);
        }
        return msg;
    } 
}
