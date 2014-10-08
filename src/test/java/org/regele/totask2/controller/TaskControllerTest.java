package org.regele.totask2.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
 * testing basic spring mvc TaskController.
 * @author Manfred
 * @since 2014-08-22
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TaskControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(TaskControllerTest.class);

    @Autowired
    private ProjectController taskController;
        

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {        
        MockitoAnnotations.initMocks(this); // process mock annotations        
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); // setup spring test in web mode (same config as spring-boot)
    }
   
    /** testing /project/1/tasks. */
    @Test
    public void testGetTasksForProject1() throws Exception {
        
        LOG.debug("request /project/1/tasks");
        
        MvcResult result =
        this.mockMvc.perform(get("/project/1/tasks"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("develop")))
        .andExpect(content().string(containsString("support")))
        .andExpect(content().string(containsString("document")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }
    
    /** testing /task/3. */
    @Test
    public void testGetEditProject() throws Exception {
        
        LOG.debug("request /task/3");
        
        MvcResult result =
        this.mockMvc.perform(get("/task/3"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("document")))
        .andExpect(content().string(containsString("3")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }
    
    /** testing /project/1/new. */
    @Test
    public void testGetNewTaskForProject() throws Exception {
        
        LOG.debug("request /project/1/task/new");
        
        MvcResult result =
        this.mockMvc.perform(get("/project/1/task/new"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("readonly")))
        .andExpect(content().string(containsString("0")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }    

    /** testing /project/999/new (non existing project). */
    @Test
    public void testGetNewTaskOnWrongProject() throws Exception {
        
        LOG.debug("request /project/999/task/new");
        
        MvcResult result =
        this.mockMvc.perform(get("/project/999/task/new"))
        .andExpect(status().isNotFound())
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }
    
    /** testing /task/delete not existing task 99999. */
    @Test
    public void testDeleteTask() throws Exception {
        
        LOG.debug("request /task/delete");
        
        MvcResult result =
        this.mockMvc.perform(post("/task/delete").param("id", "99999"))
        .andExpect(status().isNotFound())
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }  
    
    /** testing (invalid) /task/save POST. */
    @Test
    public void testInvalidSaveTask() throws Exception {
        
        LOG.debug("request /task/save");
        
        MvcResult result =
        this.mockMvc.perform(
                post("/task/save")
                .param("id", "0")
                .param("name", "x") // to short
                )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("name must be between 2 and 250 characters")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }     
    
}
