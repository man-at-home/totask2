package org.manathome.totask2.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.manathome.totask2.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** testing User data Model. 
 * 
 * @see     User
 * @author  man-at-home
 * */
public class UserTest {

    private static final Logger LOG = LoggerFactory.getLogger(UserTest.class);
    
    @Test
    public void testUserCreation() {
    
        User user = new User();
        user.setDisplayName("test user transient");
        user.setId(2);
        user.setUsername("tst");
        user.setActive(true);
        assertTrue(user.isActive());
        user.setActive(false);
        assertFalse(user.isActive());
        assertFalse(user.isAdmin());
        assertTrue(user.toString().contains("tst"));
        assertFalse(user.isEnabled());
        user.setActive(true);
        assertTrue(user.isEnabled());
        assertTrue(user.isActive() == user.isEnabled() == user.isAccountNonExpired() == user.isAccountNonLocked());
    }
    
    public void changeUser() {
        User user = new User();
        user.setDisplayName("test user transient");

        user.changePasswort("new pw"); 
        
        user.setAdmin(false);
        assertEquals("no admin role" , 0, user.getAuthorities().size()); // no admin        
        user.setAdmin(true);
        assertEquals("admin role" , 1, user.getAuthorities().size()); // admin
        LOG.debug("user: " + user);
    }

}
