package org.manathome.totask2.util;



/** simple assert. */
public final class ApplicationAssert {

    public static void assertTrue(final String message, final boolean condition) {
        assert condition;
        if (!condition){
            throw new AssertionError(message);
        }
    } 


}
