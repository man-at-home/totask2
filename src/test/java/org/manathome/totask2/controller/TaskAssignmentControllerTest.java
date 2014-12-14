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
 * testing task assignments controller.
 * 
 * @author man-at-home
 * @since  2014-12-14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TaskAssignmentControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(TaskAssignmentControllerTest.class);

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

    
    /** testing task/1/assignments. (lists current assignments). */
    @Test
    public void testAssignmentsForTask() throws Exception {
        
        LOG.debug("request /task/1/assignments");
        
        MvcResult result =
        this.mockMvc.perform(get("/task/1/assignments")
                .with(user("unit-test-admin").roles("ADMIN", "USER"))                               
                )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("task assignments to user")))
        .andExpect(content().string(containsString("assign new user to task..")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    } 
    
    /** testing task/1/assignment/new. (add new assignment to task). */
    @Test
    public void testNewTaskAssignment() throws Exception {
        
        LOG.debug("request /task/1/assignment/new");
        
        MvcResult result =
        this.mockMvc.perform(get("/task/1/assignment/new")
                .with(user("unit-test-admin").roles("ADMIN", "USER"))                               
                )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("userIdRef")))
        .andExpect(content().string(containsString(".autocomplete")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }   

}
