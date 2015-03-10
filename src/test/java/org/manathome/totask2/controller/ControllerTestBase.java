package org.manathome.totask2.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.manathome.totask2.Application;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/** common code tests. */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public abstract class ControllerTestBase {

    @Autowired
    protected WebApplicationContext wac;
    
    @Autowired
    private FilterChainProxy springSecurityFilterChain;    

    protected MockMvc mockMvc;
    

    @Before
    public void setup() {        
        MockitoAnnotations.initMocks(this); // process mock annotations        
        
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilters(this.springSecurityFilterChain)
                .build(); // setup spring test in web mode (same config as spring-boot)
        
    }
}
