package org.manathome.totask2.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.manathome.totask2.Application;
import org.manathome.totask2.SwaggerConfig;
import org.manathome.totask2.model.Project;
import org.manathome.totask2.model.ProjectRepository;
import org.manathome.totask2.service.ProjectPlanData;
import org.manathome.totask2.service.ProjectPlanDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import javax.transaction.Transactional;


/** test data only. */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, SwaggerConfig.class })
@WebAppConfiguration
public class PlanDataTest {
    
    @Autowired private ProjectRepository      projectRepository;   
    @Autowired private ProjectPlanDataService projectPlanDataService;
    
    @Test
    public void testPlanData() {
        
        Project project = projectRepository.getOne(2L);
        List<ProjectPlanData> planData = projectPlanDataService.createProjectPlanData(project);

        assertNotNull(planData);
        assertTrue(planData.size() >= 1);
        ProjectPlanData ppd = planData.get(0);
        
        assertNotNull(ppd.getName());
        assertNotNull(ppd.getSeries());
    }

}
