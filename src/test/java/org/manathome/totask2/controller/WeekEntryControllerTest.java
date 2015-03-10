package org.manathome.totask2.controller;

import static org.hamcrest.Matchers.containsString;
import static org.manathome.totask2.controller.SecurityRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.manathome.totask2.Application;
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
 * testing basic spring mvc WeekEntryController.
 * 
 * @see    WeekEntryController
 * @author man-at-home
 * @since  2015-03-09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class WeekEntryControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WeekEntryControllerTest.class);

    @Autowired private WebApplicationContext    wac;
    @Autowired private FilterChainProxy         springSecurityFilterChain; 

    private MockMvc mockMvc;

    @Before
    public void setup() {        
        MockitoAnnotations.initMocks(this); // process mock annotations        
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilters(this.springSecurityFilterChain)
                .build(); // setup spring test in web mode (same config as spring-boot)
    }
   
    /** testing GET /weekEntry. 
     * 
     * @see WeekEntryController#weekEntry(org.springframework.ui.Model) under test.
     * */
    @Test
    public void testCurrentWeekEntry() throws Exception {
        
        LOG.debug("request /weekEntry");
        
        MvcResult result =
        this.mockMvc.perform(get("/weekEntry")
                .with(user("unit-test-user").roles("USER"))                
                )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("enter this")))
        .andExpect(content().string(containsString("save this week")))
        .andExpect(content().string(containsString("form id=\"weekForm\"")))        
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }
        
}
