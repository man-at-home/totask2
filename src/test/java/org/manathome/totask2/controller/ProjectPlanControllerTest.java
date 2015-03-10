package org.manathome.totask2.controller;

import static org.hamcrest.Matchers.containsString;
import static org.manathome.totask2.controller.SecurityRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

/** test gant plan creation. */
public class ProjectPlanControllerTest  extends ControllerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectPlanControllerTest.class);

    @Autowired
    private ProjectPlanController projectPlanController;       


    /** testing /plan/project/1. */
    @Test
    public void testPlanProject1() throws Exception {
        
        LOG.debug("request /plan/project/1");
                
        MvcResult result =
        this.mockMvc.perform(get("/plan/project/1")
                .with(user("unit-test-admin").roles("ADMIN"))
                )               
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("jquery.ganttView.js")))       
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }

}
