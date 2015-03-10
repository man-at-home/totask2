package org.manathome.totask2.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.manathome.totask2.util.AAssert.checkNotNullOrEmpty;

import org.junit.Test;

import java.math.BigDecimal;


/** 
 * testing AAssert. 
 * 
 * @see AAssert
 */
public class AAssertTest {

    /** test checkNotNull.*/
    @Test
    public void testCheckNotNull() {
        
        assertEquals("xx", AAssert.checkNotNull("xx"));
        assertEquals("", AAssert.checkNotNull(""));
        assertEquals(55, AAssert.checkNotNull(new BigDecimal("55")).intValue());
    }
    
    /** test checkNotNull(null).*/
    @Test(expected = NullPointerException.class)
    public void testCheckNotNullWithNull() {
        
        AAssert.checkNotNull(null);
        fail("no exception");
    }
    
    

    /** test checkNotNull(null).*/
    @Test
    public void testCheckNotOrEmpty() {
        
        assertNotNull(checkNotNullOrEmpty("filled", "valid"));
    }
    /** test checkNotNull(null).*/
    @Test(expected = NullPointerException.class)
    public void testCheckNotOrEmptyFailed() {
        
        assertNotNull(checkNotNullOrEmpty(null, "invalid"));
    }
    /** test checkNotNull(null).*/
    @Test(expected = AssertionError.class)
    public void testCheckNotOrEmptyFailedEmpty() {
        
        assertNotNull(checkNotNullOrEmpty("", "invalid"));
    }

    /** 0 to large.*/
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndex0AboveBounds() {
        
        AAssert.checkIndex(0, 0);
        fail("no exception");
    }
    
    /** 1 to large.*/
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexAboveBounds() {
        
        AAssert.checkIndex(1, 1);
        fail("no exception");
    }
    
    /** -1 never. */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexBelowBounds() {
        
        AAssert.checkIndex(1, -1);
        fail("no exception");
    }
    
    /** 4 is in bounds. */
    @Test
    public void testIndexInBounds() {
        
        assertEquals(4, AAssert.checkIndex(5, 4));
    }
    
    /** testing assertTrue (true). */
    @Test
    public void testTrue() {
       AAssert.checkTrue(true, "true");
    }
    
    
    /** testing assertTrue(false). */
    @Test(expected = AssertionError.class)
    public void testFalseException() {
       AAssert.checkTrue(false, "throws exception");
    }
    
}
