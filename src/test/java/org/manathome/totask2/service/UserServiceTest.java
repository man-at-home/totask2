package org.manathome.totask2.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.manathome.totask2.Application;
import org.manathome.totask2.SwaggerConfig;
import org.manathome.totask2.util.Authorisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

/**
 * user access.
 * 
 * @author man-at-home
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, SwaggerConfig.class })
@WebAppConfiguration
public class UserServiceTest {

    @Autowired UserDetailsServiceImpl userService;
    
    @Test
    public void testLoadUserDetails() {
        
        UserDetails u = userService.loadUserByUsername("unit-test-admin");
        assertNotNull("user not found", u);
        assertNotNull("user name", u.getUsername());
        assertNotNull("user pw", u.getPassword());
        assertTrue("this user should be admin", u.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals(Authorisation.ROLE_ADMIN)));
        assertTrue("this user should be monitor", u.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals(Authorisation.ROLE_MONITOR)));           
        assertTrue("this user should have user-role", u.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals(Authorisation.ROLE_USER)));       
    }
}

