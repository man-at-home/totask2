package org.manathome.totask2.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.manathome.totask2.controller.SecurityRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.manathome.totask2.model.User;
import org.manathome.totask2.service.UserCachingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.Charset;
import java.util.List;


/**
 * testing user caching.
 * @author man-at-home
 * @since 2014-10-28
 */
public class UserControllerTest extends ControllerTestBase {
    
    private static final Logger LOG = LoggerFactory.getLogger(UserControllerTest.class);    

    @Autowired private UserCachingService       userCachingService;
    
    /** testing /users (all). */
    @Test
    public void testCachedUsers() throws Exception {
        
        LOG.debug("caching users?");
        List<User> users = userCachingService.getCachedUsers();
        assertNotNull("no users", users);
        LOG.debug("  users " + users.size());
        users = userCachingService.getCachedUsers(); // should be cached.
        LOG.debug("  users " + users.size());
        users = userCachingService.getCachedUsers(); // should be cached.
        LOG.debug("  users " + users.size());
    }    
    
    /** testing /users (all). */
    @Test
    public void testUsers() throws Exception {
        
        LOG.debug("request /users");
        
        MvcResult result =
        this.mockMvc
            .perform(get("/users")
            .with(user("unit-test-admin")
            .roles("ADMIN", "USER")))
        .andExpect(status().isOk())
        .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(),                        
                Charset.forName("utf8")                     
                )))
        .andExpect(content().string(containsString("\"displayName\"")))
        .andExpect(content().string(containsString("\"predefined admin user\"")))
        .andExpect(content().string(containsString("\"unit-test user\"")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }

    
    /** testing /users?filter=admin. */
    @Test
    public void testUsersWithAdmin() throws Exception {
        
        LOG.debug("request /users?term=a");
        
        MvcResult result =
        this.mockMvc
            .perform(get("/users")
                    .param("term", "admin")                  
            .with(user("unit-test-admin")
            .roles("ADMIN", "USER")))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("\"displayName\"")))
        .andExpect(content().string(containsString("\"predefined admin user\"")))
        .andExpect(content().string(containsString("\"admin\"")))
        .andExpect(content().string(not(containsString("\"unit-test-user\"")))) // other user in db.
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }
  

}
