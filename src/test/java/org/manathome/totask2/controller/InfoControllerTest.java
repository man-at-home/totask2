package org.manathome.totask2.controller;

import static org.hamcrest.Matchers.containsString;
import static org.manathome.totask2.controller.SecurityRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.manathome.totask2.Application;
import org.manathome.totask2.controller.InfoController;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 * testing basic spring mvc InfoController.
 * @author man-at-home
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class InfoControllerTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(InfoControllerTest.class);

    @Autowired
    private InfoController infoController;

    @Autowired
    private WebApplicationContext wac;
    
    @Autowired
    private FilterChainProxy springSecurityFilterChain;    


    private MockMvc mockMvc;

    @Before
    public void setup(){

        // Process mock annotations
        MockitoAnnotations.initMocks(this);

        // Setup Spring test in webapp-mode (same config as spring-boot)
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilters(this.springSecurityFilterChain)    
                .build();
    }
   
    /** testing /dbinfo. */
    @Test
    public void testGetDbInfo() throws Exception {
        
        LOG.debug("request /dbinfo");
        
        MvcResult result =
        this.mockMvc.perform(get("/dbinfo")
                .with(user("unit-test-admin").roles("ADMIN"))                
                )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("tasks in database:")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }
    

}
