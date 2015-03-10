package org.manathome.totask2.controller;

import static org.hamcrest.Matchers.containsString;
import static org.manathome.totask2.controller.SecurityRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;


/**
 * testing basic spring mvc ProjectController.
 * @author man-at-home
 */
public class ProjectControllerTest extends ControllerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectControllerTest.class);

    @Autowired
    private ProjectController projectController;       


    
   
    /** testing /projects. */
    @Test
    public void testGetProjects() throws Exception {
        
        LOG.debug("request /projects");
                
        MvcResult result =
        this.mockMvc.perform(get("/projects")
                .with(user("unit-test-admin").roles("ADMIN"))
                )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("totask2")))
        .andExpect(content().string(containsString("demo-project")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }
    
    /** testing /project/1. */
    @Test
    public void testGetEditProject() throws Exception {
        
        LOG.debug("request /project/1");
        
        MvcResult result =
        this.mockMvc.perform(get("/project/1")
                        .with(user("unit-test-admin").roles("ADMIN", "USER"))
                )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("totask2")))
        .andExpect(content().string(containsString("1")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }
    
    /** testing /project/new. */
    @Test
    public void testGetNewProject() throws Exception {
        
        LOG.debug("request /project/new");
        
        MvcResult result =
        this.mockMvc.perform(get("/project/new")
                        .with(user("unit-test-admin").roles("ADMIN", "USER"))
                )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("readonly")))
        .andExpect(content().string(containsString("0")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    } 
    
    /** testing (invalid) /project/save POST. */
    @Ignore("post does not work with authentication as is..")
    @Test
    public void testInvalidSaveProject() throws Exception {
        
        LOG.debug("request /project/save");
        
        MvcResult result =
        this.mockMvc.perform(
                post("/project/save")
  //              .with(user("unit-test-admin").roles("ADMIN", "USER"))
                .param("id", "0")
                .param("name", "x") // to short
                )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("name must be between 2 and 250 characters")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    } 
    
    /** testing /projects/report jasper generated pdf. */
    @Test
    public void testProjectReport() throws Exception {
        
        LOG.debug("request /projects/report/pdf");
        
        this.mockMvc.perform(get("/projects/report/pdf")
                .with(user("unit-test-admin").roles("ADMIN", "USER"))
        )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("%PDF")))
        .andReturn();        
    }
    
}
