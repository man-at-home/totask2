package org.manathome.totask2.util;

import static org.junit.Assert.*;

import org.junit.Test;

/** 
 * test the authorization helper. 
 * 
 * @author man-at-home
 */
public class AuthorisationTest {

    @Test
    public void testRequiredTrue() {
        Authorisation.require(true);
    }

    /** not fulfilled. */
    @Test(expected = NotAllowedException.class)     
    public void testRequiredFalse() {
        Authorisation.require(false);
        fail("NotAuthorised expected");
    }
    
}
