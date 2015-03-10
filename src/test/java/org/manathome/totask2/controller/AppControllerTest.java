package org.manathome.totask2.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**  test landing page .*/
public class AppControllerTest extends ControllerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(AppControllerTest.class);

    /** get /. */
    @Test
    public void testHealthEndpoint() throws Exception {

        LOG.debug("request /");
        
        this.mockMvc.perform(get("/"))
        .andDo(print())  
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("projects")))
        .andReturn();   
    }
    
}
