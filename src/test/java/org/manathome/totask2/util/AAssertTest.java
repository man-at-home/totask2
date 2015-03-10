package org.manathome.totask2.util;

import static org.junit.Assert.*;

import org.junit.Test;
import java.math.BigDecimal;


/** testing AAssert. 
 * 
 * @see AAssert
 * */
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
    
    @Test
    public void testTrue() {
       AAssert.assertTrue("true", true);
    }
    
    @Test(expected = AssertionError.class)
    public void testFalseException() {
       AAssert.assertTrue("throws exception", false);
    }
    
}
