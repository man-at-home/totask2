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
}
