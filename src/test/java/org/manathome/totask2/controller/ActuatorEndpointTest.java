package org.manathome.totask2.controller;

import static org.hamcrest.Matchers.containsString;
import static org.manathome.totask2.controller.SecurityRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** test monitoring /health and /metrics endpoints. */
public class ActuatorEndpointTest extends ControllerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(ActuatorEndpointTest.class);

    @Test
    public void testHealthEndpoint() throws Exception {

        LOG.debug("request /monitor/health");
        
        this.mockMvc.perform(get("/monitor/health")
                .with(user("monitor").roles("MONITOR"))
                )
        .andDo(print())  
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("projectsInDatabase")))
        .andReturn();   
    }
    
    /** remote monitoring endpoint. */
    @Test
    public void testMetricsEndpoint() throws Exception {

        LOG.debug("request /monitor/metrics");
        
        this.mockMvc.perform(get("/monitor/metrics")
                .with(user("monitor").roles("MONITOR"))
                )
        .andDo(print())  
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("TaskInWeek")))  // own metrics injected..
        .andReturn();   
    }

}
