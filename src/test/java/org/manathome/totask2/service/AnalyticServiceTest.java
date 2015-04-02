package org.manathome.totask2.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.manathome.totask2.Application;
import org.manathome.totask2.SwaggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


/** test (no exception only). */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, SwaggerConfig.class })
@WebAppConfiguration
public class AnalyticServiceTest {

    @Autowired AnalyticService analyticService;

    /** no exception here.. */
    @Test
    public void testServiceInjection() {        
        assertNotNull(analyticService);
    }
   
    /** no exception here.. */
    @Test
    public void testLogData() {        
        analyticService.logData("junit-test", "key", "arbitraryData");
    }

    /** no exception here.. */
    @Test
    public void testLogMasterDataChanges() {        
        long rnd = Math.round(Math.random() * 1000);
        analyticService.logMasterDataChanges("junitEntity", rnd, "dummy-name for " + rnd);
    }

}
