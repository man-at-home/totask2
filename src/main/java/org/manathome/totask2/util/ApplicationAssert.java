package org.manathome.totask2.util;



/** simple assert. */
public final class ApplicationAssert {

    public static void assertTrue(final String message, final boolean condition) {
        assert condition;
        if (!condition){
            throw new AssertionError(message);
        }
    }

    public static void assertNotNull(final String message, final Object object) {
        assert object != null;
        if (object == null) {
            throw new AssertionError(message);
        }
    }    

    public static void assertNotNullOrEmpty(final String message, final String nonEmptyString) {
        // assert nonEmptyString != null && nonEmptyString.trim().length() > 0;
        if (nonEmptyString == null || nonEmptyString.trim().length() == 0) {
            throw new AssertionError(message);
        }
    } 
}
