package org.regele.totask2.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import static org.regele.totask2.controller.SecurityRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.regele.totask2.Application;
import org.regele.totask2.model.User;
import org.regele.totask2.service.UserCachingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.List;


/**
 * testing user caching.
 * @author man-at-home
 * @since 2014-10-28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class UserControllerTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(UserControllerTest.class);    

    private MockMvc mockMvc;
    @Autowired private WebApplicationContext    wac;
    @Autowired private FilterChainProxy         springSecurityFilterChain; 
    @Autowired private UserCachingService       userCachingService;
    
    @Before
    public void setup() {        
        MockitoAnnotations.initMocks(this); // process mock annotations        
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilters(this.springSecurityFilterChain)
                .build(); // setup spring test in web mode (same config as spring-boot)
    }    
 
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
        .andExpect(content().string(containsString("\"displayName\":\"predefined admin user\"")))
        .andExpect(content().string(containsString("\"username\":\"admin\"")))
        .andExpect(content().string(containsString("\"displayName\":\"unit-test user\"")))
        .andExpect(content().string(containsString("\"username\":\"unit-test-user\"")))
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
        .andExpect(content().string(containsString("\"displayName\":\"predefined admin user\"")))
        .andExpect(content().string(containsString("\"username\":\"admin\"")))
        .andExpect(content().string(not(containsString("\"username\":\"unit-test-user\""))))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }
  

}
