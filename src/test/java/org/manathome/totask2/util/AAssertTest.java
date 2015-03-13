package org.manathome.totask2.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.manathome.totask2.util.AAssert.checkNotNullOrEmpty;

import org.junit.Test;

import java.math.BigDecimal;


/** 
 * testing AAssert. 
 * 
 * uses for syntactical sugar hamcrest matchers.
 * 
 * @see <a href="https://code.google.com/p/hamcrest/">hamcrest</a>
 * @see AAssert
 */
public class AAssertTest {

    /** test checkNotNull.*/
    @Test
    public void testCheckNotNull() {
        assertThat(AAssert.checkNotNull("xx"), is("xx"));
        assertThat(AAssert.checkNotNull(""), is(""));
        assertThat("check 54 should return 54", AAssert.checkNotNull(new BigDecimal("54")).intValue(), is(54));
    }
    
    /** test checkNotNull(null).*/
    @Test(expected = NullPointerException.class)
    public void testCheckNotNullWithNull() {
        AAssert.checkNotNull(null);
    }

    /** test checkNotNull(null).*/
    @Test
    public void testCheckNotOrEmpty() {
        assertThat(AAssert.checkNotNullOrEmpty("filled", "valid"), notNullValue());
    }
    
    /** test checkNotNull(null).*/
    @Test(expected = NullPointerException.class)
    public void testCheckNotOrEmptyFailed() {
        
        assertThat(checkNotNullOrEmpty(null, "invalid"), notNullValue());
    }
    /** test checkNotNull(null).*/
    @Test(expected = AssertionError.class)
    public void testCheckNotOrEmptyFailedEmpty() {
        assertThat(checkNotNullOrEmpty("", "invalid"), notNullValue());
    }

    /** 0 to large.*/
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndex0AboveBounds() {        
        AAssert.checkIndex(0, 0);
    }
    
    /** 1 to large.*/
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexAboveBounds() {
        AAssert.checkIndex(1, 1);
    }
    
    /** -1 never. */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexBelowBounds() {        
        AAssert.checkIndex(1, -1);
    }
    
    /** 4 is in bounds. */
    @Test
    public void testIndexInBounds() {
        assertThat(AAssert.checkIndex(5, 4), is(4));
    }
  
    
    /** testing assertTrue(false). */
    @Test(expected = AssertionError.class)
    public void testFalseException() {
       AAssert.checkTrue(false, "throws exception");
    }
    
}
