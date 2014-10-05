package org.regele.totask2.util;



/** simple assert  */
public final class ApplicationAssert {

    public static void assertTrue(String message, boolean condition) {
        assert condition;
        if( ! condition )
            throw new AssertionError(message);
    }

    public static void assertNotNull(String message, Object object) {
        assert object != null;
        if( object == null)
            throw new AssertionError(message);
    }    

    public static void assertNotNullOrEmpty(String message, String nonEmptyString) {
        assert nonEmptyString != null && nonEmptyString.trim().length() > 0;
        if( nonEmptyString == null || nonEmptyString.length() == 0 || nonEmptyString.trim().length() == 0 )
            throw new AssertionError(message);
    } 
}
