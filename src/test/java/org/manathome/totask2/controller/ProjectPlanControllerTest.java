package org.manathome.totask2.controller;

import static org.hamcrest.Matchers.containsString;
import static org.manathome.totask2.controller.SecurityRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.manathome.totask2.util.TestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.Charset;


/** test gant plan creation. */
public class ProjectPlanControllerTest  extends ControllerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectPlanControllerTest.class);

    // @Autowired private ProjectPlanController projectPlanController;       


    /** testing /plan/project/1. */
    @Test
    public void testPlanProject1() throws Exception {
        
        LOG.debug("request /plan/project/" + TestConstants.TEST_PROJECT);
                
        MvcResult result =
        this.mockMvc.perform(get("/plan/project/" + TestConstants.TEST_PROJECT)
                .with(user("unit-test-admin").roles("ADMIN"))
                )               
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("jquery.ganttView.js")))       
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }

    
    /** testing /REST/plan/project/{id}. */
    @Test
    public void testPlanRestDataProject1() throws Exception {
        
        LOG.debug("request /REST/plan/project/{id}");
             
        MvcResult result =
        this.mockMvc.perform(
                 get("/REST/plan/project/" + TestConstants.TEST_PROJECT)
                .with(user("unit-test-admin").roles("ADMIN"))
                )
        .andDo(print())
        .andExpect(status().isOk())  
        .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(),                        
                Charset.forName("utf8")                     
                )))        
        .andExpect(content().string(containsString("\"name\"")))
        .andExpect(content().string(containsString("T00:00")))   
        .andReturn();
        

        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }
    

}
