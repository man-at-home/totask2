package org.regele.totask2.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.regele.totask2.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * testing basic spring mvc ProjectController.
 * @author Manfred
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ProjectControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectControllerTest.class);

    @Autowired
    private ProjectController projectController;
        

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {        
        MockitoAnnotations.initMocks(this); // process mock annotations        
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); // setup spring test in web mode (same config as spring-boot)
    }
   
    /** testing /projects. */
    @Test
    public void testGetProjects() throws Exception {
        
        LOG.debug("request /projects");
        
        MvcResult result =
        this.mockMvc.perform(get("/projects"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("totask2")))
        .andExpect(content().string(containsString("demo-project")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }
    
    /** testing /projects. */
    @Test
    public void testGetEditProject() throws Exception {
        
        LOG.debug("request /editProject");
        
        MvcResult result =
        this.mockMvc.perform(get("/editProject").param("editProject", "1"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("totask2")))
        .andExpect(content().string(containsString("1")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }    

}
