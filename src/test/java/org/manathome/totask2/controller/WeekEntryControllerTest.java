package org.manathome.totask2.controller;

import static org.hamcrest.Matchers.containsString;
import static org.manathome.totask2.controller.SecurityRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

/**
 * testing basic spring mvc WeekEntryController.
 * 
 * @see    WeekEntryController
 * @author man-at-home
 * @since  2015-03-09
 */
public class WeekEntryControllerTest extends ControllerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(WeekEntryControllerTest.class);
   
    /** testing GET /weekEntry. 
     * 
     * @see WeekEntryController#weekEntry(org.springframework.ui.Model) under test.
     * */
    @Test
    public void testCurrentWeekEntry() throws Exception {
        
        LOG.debug("request /weekEntry");
        
        MvcResult result =
        this.mockMvc.perform(get("/weekEntry")
                .with(user("unit-test-user").roles("USER"))                
                )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("enter this")))
        .andExpect(content().string(containsString("save this week")))
        .andExpect(content().string(containsString("form id=\"weekForm\"")))        
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }
    
    /** 
     * testing GET /weekEntry. /weekEntry/report/excel/(date)
     * 
     * @see WeekEntryController#weekEntry(org.springframework.ui.Model) under test.
     * @see https://github.com/man-at-home/totask2/issues/34
     * @since 2015-06-26
     */
    @Test
    public void testCurrentWeekEntryExcelReport() throws Exception {
        
        LocalDate dt = LocalDate.now();
        LOG.debug("request /weekEntry/report/excel/" + dt.toString());
        
        MvcResult result =
        this.mockMvc.perform(get("/weekEntry/report/excel/" + dt.toString())
                .with(user("unit-test-user").roles("USER"))                
                )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/vnd.ms-excel"))      
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentLength() + " bytes as excel report");
    }    
        
}
