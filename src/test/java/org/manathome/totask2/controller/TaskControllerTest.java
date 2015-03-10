package org.manathome.totask2.controller;

import static org.hamcrest.Matchers.containsString;
import static org.manathome.totask2.controller.SecurityRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MvcResult;

/**
 * testing basic spring mvc TaskController.
 * @author man-at-home
 * @since 2014-08-22
 */
public class TaskControllerTest extends ControllerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(TaskControllerTest.class);
   
    /** testing /project/1/tasks. */
    @Test
    public void testGetTasksForProject1() throws Exception {
        
        LOG.debug("request /project/1/tasks");
        
        MvcResult result =
        this.mockMvc.perform(get("/project/1/tasks")
                .with(user("unit-test-admin").roles("ADMIN", "USER"))                
                )
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
        this.mockMvc.perform(get("/task/3")
                .with(user("unit-test-admin").roles("ADMIN", "USER"))                               
                )
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
        this.mockMvc.perform(get("/project/1/task/new")
                .with(user("unit-test-admin").roles("ADMIN", "USER"))                               
                )
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
        this.mockMvc.perform(get("/project/999/task/new")
                .with(user("unit-test-admin").roles("ADMIN", "USER"))                               
                )
        .andExpect(status().isNotFound())
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }
    
    /** testing /task/delete not existing task 99999. */
    @Ignore("post does not work with authentication as is..")
    @Test
    public void testDeleteTask() throws Exception {
        
        LOG.debug("request /task/delete");
        
        MvcResult result =
        this.mockMvc.perform(
                post("/task/delete")
                .param("id", "99999")
                .with(user("unit-test-admin").roles("ADMIN", "USER"))                               
                )
        .andExpect(status().isNotFound())
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }  
    
    /** testing (invalid) /task/save POST. */
    @Ignore("post does not work with authentication as is..")
    @Test
    public void testInvalidSaveTask() throws Exception {
        
        LOG.debug("request /task/save");
        
        MvcResult result =
        this.mockMvc.perform(
                post("/task/save")
                .param("id", "0")
                .param("name", "x") // to short
                .with(user("unit-test-admin").roles("ADMIN", "USER"))                 
                )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("name must be between 2 and 250 characters")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }     
    
}
