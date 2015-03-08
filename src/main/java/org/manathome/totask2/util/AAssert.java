package org.manathome.totask2.util;

import javax.validation.constraints.NotNull;

/** checking arguments (convenience class). 
 * 
 * @author man-at-home
 * */
public class AAssert {

    private AAssert() {}
    
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


    /** 
     * assert condition. 
     * 
     * @throws AssertionError
     * */
    public static void assertTrue(@NotNull final String message, final boolean condition) {
        if (!condition){
            throw new AssertionError(message);
        }
    }
    
    /** check index bound, requires index >= arraySize-1. 
     * @throws IndexOutOfBoundsException 
     */
    public static int checkIndex(int arraySize, int index) {
        if (index < 0 || index > arraySize - 1) {
            throw new IndexOutOfBoundsException("Index " + index + " not in bounds [0.." + arraySize + "]");
        }
        return index;
    } 
}
