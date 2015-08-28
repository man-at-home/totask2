package org.manathome.totask2.controller;

import static org.hamcrest.Matchers.containsString;
import static org.manathome.totask2.controller.SecurityRequestPostProcessors.*;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.Test;
import org.manathome.totask2.model.Project;
import org.manathome.totask2.model.ProjectRepository;
import org.manathome.totask2.util.ProjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;


/**
 * testing basic spring mvc ProjectController.
 * @author man-at-home
 */
public class ProjectControllerTest extends ControllerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectControllerTest.class);

//    @Autowired private ProjectController projectController;       

    @Autowired private ProjectRepository projectRepository;

    
   
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
    // @Ignore("post does not work with csrf enabled as is..")
    @Test
    public void testInvalidSaveProject() throws Exception {
        
        LOG.debug("post invalid /project/save");
                  
        MvcResult result =
        this.mockMvc.perform(
                post("/project/save")
             //   .with(csrf())
                .with(user("unit-test-admin").roles("ADMIN", "USER"))
                .param("id", "0")
                .param("name", "j") // to short
                )
        .andDo(print())                       // output system.out
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("name must be between 2 and 250 characters")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    } 
    
    
    /** testing insert via /project/save POST. */
    // @Ignore("post does not work with csrf enabled as is..")
    @Test
    @Transactional
    public void testSaveProjectPost() throws Exception {
        
        String name = "junit-project-save";
        LOG.debug("post /project/save project: " + name);
                  
        this.mockMvc.perform(
                post("/project/save")
             //   .with(csrf())
                .with(user("unit-test-admin").roles("ADMIN", "USER"))
                .param("id", "0")
                .param("name", name)
                )
        .andDo(print())                       // output system.out
        .andExpect(status().is3xxRedirection())
        .andReturn();
        
        Project p = projectRepository.findByName(name).stream().findFirst().orElseThrow(() -> new ProjectNotFoundException());
        LOG.debug("project " + p + " stored.");
        projectRepository.delete(p.getId());
        projectRepository.flush();
    }
    
    /** testing update via /project/save POST. */
    // @Ignore("post does not work with csrf enabled as is..")
    @Test
    @Transactional
    public void testUpdateProjectPost() throws Exception {
        
        String name = "junit-project-update";
        String changedName = "changed-project";
        LOG.debug("post /project/save project: " + name);

        Project p = new Project();
        p.setName(name);
        p = projectRepository.save(p);
        projectRepository.flush();
        
        this.mockMvc.perform(
                post("/project/save")
             //   .with(csrf())
                .with(user("unit-test-admin").roles("ADMIN", "USER"))
                .param("id", p.getId() + "")
                .param("name", changedName)
                )
        .andDo(print())                       // output system.out
        .andExpect(status().is3xxRedirection())
        .andReturn();
        
        p = projectRepository.findByName(changedName).stream().findFirst().orElseThrow(() -> new ProjectNotFoundException());
        LOG.debug("project " + p + " updated.");
        projectRepository.delete(p.getId());
        projectRepository.flush();
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
