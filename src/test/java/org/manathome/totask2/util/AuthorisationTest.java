package org.manathome.totask2.util;

import static org.hamcrest.Matchers.*;
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
    }

    /** base test only. */
    public void testIsAuthenticated() {
        assertThat(Authorisation.isAdmin(null), is(false));
    }
    
}
