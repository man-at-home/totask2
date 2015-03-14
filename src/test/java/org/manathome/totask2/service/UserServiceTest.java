package org.manathome.totask2.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.manathome.totask2.Application;
import org.manathome.totask2.SwaggerConfig;
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
        assertEquals("admin and user role expected, but got " + u, 2, u.getAuthorities().size());        
    }

}
