package org.manathome.totask2.model;

import static org.junit.Assert.*; 

import org.junit.Test;
import org.manathome.totask2.util.Authorisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * testing User data Model.
 * 
 * @see User
 * @author man-at-home
 * */
public class UserTest {

    private static final Logger LOG = LoggerFactory.getLogger(UserTest.class);

    @Test
    public void testUserDumps() {
        User user = new User();
        user.setDisplayName("test user transient 0");
        User.dumpAuthentication();
        assertNotNull(user.toString());
    }

    @Test
    public void testUserCreation() {

        User user = new User();
        user.setDisplayName("test user transient");
        user.setId(2);
        user.setUsername("tst");
        user.setActive(true);
        assertTrue(user.isActive());
        assertTrue(user.isCredentialsNonExpired());
        user.setActive(false);
        assertFalse(user.isActive());
        assertFalse(user.isAdmin());
        user.setAdmin(true);
        assertTrue(user.isAdmin());
        assertTrue(user.toString().contains("tst"));
        assertFalse(user.isEnabled());
        user.setActive(true);
        assertTrue(user.isEnabled());
        assertTrue(user.isActive() == user.isEnabled() == user
                .isAccountNonExpired() == user.isAccountNonLocked());

        user.changePasswort("xx2");
        assertFalse(user.getPassword().equals("xx"));
        String oldpw = user.getPassword();
        user.changePasswort("2342343242");
        assertFalse(oldpw.equals(user.getPassword()));

    }

    @Test
    public void testChangeUser() {
        User user = new User();
        user.setDisplayName("test user transient");

        user.changePasswort("new pw");

        user.setAdmin(false);
        assertTrue("no admin role expected", !user.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals(Authorisation.ROLE_ADMIN)));
        user.setAdmin(true);
        assertTrue("admin role expected", user.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals(Authorisation.ROLE_ADMIN)));
        user.setAdmin(false);
        assertTrue("no admin role expected", !user.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals(Authorisation.ROLE_ADMIN)));
        LOG.debug("user: " + user);
    }

    /** no monitoring role. */
    @Test
    public void testUserMonitoringRole() {
        User user = new User();
        user.setUsername("tmp-adm");
        user.setDisplayName("test monitoring user transient");

        user.setAdmin(false);
        assertTrue("no monitoring role expected", !user.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals(Authorisation.ROLE_MONITOR)));
        user.setAdmin(true);
        assertTrue("monitoring role expected", user.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals(Authorisation.ROLE_MONITOR)));
        user.setAdmin(false);
        
        user = new User();
        user.setUsername("monitor");
        user.setDisplayName("test implicit monitoring user (by name)");
        assertTrue("monitoring role expected", user.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals(Authorisation.ROLE_MONITOR)));
    }    

    /** test overridden hash and equals. */
    @Test
    public void testCompare() {
        User user = new User();
        user.setDisplayName("test user to compare");
        user.setId(2);
        user.setUsername("tst");

        User userb = new User();
        userb.setDisplayName("test user to compare");
        userb.setId(2);
        userb.setUsername("tst");

        assertEquals("hashCodes of users", user.hashCode(), userb.hashCode());
        assertTrue(user.equals(userb));
        assertTrue(userb.equals(user));

        assertTrue(!((Object) user).equals((Object) "no user"));
        assertTrue(!user.equals(null));

        userb.setUsername("tst other");

        assertFalse("hashcode should differ", user.hashCode() == userb.hashCode());
        assertTrue(!user.equals(userb));
        assertTrue(!userb.equals(user));
    }

}
