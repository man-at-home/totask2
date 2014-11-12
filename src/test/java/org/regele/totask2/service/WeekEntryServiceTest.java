package org.regele.totask2.service;


import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.regele.totask2.Application;
import org.regele.totask2.model.TaskInWeek;
import org.regele.totask2.model.User;
import org.regele.totask2.model.UserRepository;
import org.regele.totask2.model.WorkEntryRepository;
import org.regele.totask2.util.TestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.wordnik.swagger.config.SwaggerConfig;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;


/** 
 * testing db access and data cumulation for one week of work. 
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, SwaggerConfig.class })
@WebAppConfiguration
public class WeekEntryServiceTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(WeekEntryServiceTest.class);
    
    /** work entry repository under test. */
    @Autowired private WorkEntryRepository workEntryRepository;
    
    @Autowired private UserRepository userRepository;
    
    /** under test. */
    @Autowired private WeekEntryService weekEntryService;
 
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LOG.debug("testing db weekEntryService access ");
    }

    /** injection of repositories. */
    @Test   
    public void testRepoInjection() {
        assertNotNull("work entry repo not injected", workEntryRepository);
        assertNotNull("user repo not injected", userRepository);
        assertNotNull("weekEntryService not injected", weekEntryService);
    }

    
    /** find week date test-user inserted by data.sql. */
    @Test   
    public void testReadTestEntries() {

        LocalDate dt   = LocalDate.now();        
        User      user = userRepository.getOne( TestConstants.TEST_USER );
        
        List<TaskInWeek> tasksInWeek = weekEntryService.getWorkWeek(user, dt);
        
        assertNotNull("TaskInWeek found" , tasksInWeek);
        
        LOG.debug("retrieved tasks for week: " + tasksInWeek.size());
        
        tasksInWeek.forEach( tiw -> LOG.debug( tiw.getTask() + ": " + tiw.getDuration() + " hours." ));
        
        for(TaskInWeek tiw : tasksInWeek)
        {
           assertNotNull(tiw.getTask());
           assertNotNull(tiw.getDailyEntries());
           assertEquals("7 workdays for task", 7, tiw.getDailyEntries().length);
           assertFalse("some entries null", Arrays.stream(tiw.getDailyEntries()).anyMatch( we -> we == null));
           assertFalse("some entries.task null", Arrays.stream(tiw.getDailyEntries()).anyMatch( we -> we.getTask() == null));
           assertFalse("some entries.user null", Arrays.stream(tiw.getDailyEntries()).anyMatch( we -> we.getUser() == null));
           
           LOG.debug("work entry " + tiw.getTask() + " Mo: " + tiw.getDailyEntries()[0] + ", Di: " + tiw.getDailyEntries()[1]);
        }
    }   
    
    
    /** update */
    @Test   
    public void testUpdateTestEntries() {

        LocalDate dt   = LocalDate.now();        
        User      user = userRepository.getOne( TestConstants.TEST_USER );
                
        List<TaskInWeek> tasksInWeek = weekEntryService.getWorkWeek(user, dt);
        assertEquals("0 update", 0, weekEntryService.saveWeek(tasksInWeek));
        
        tasksInWeek.get(0).getDailyEntries()[0].setDuration(9.9f);
        tasksInWeek.get(0).getDailyEntries()[1].setDuration(8.8f);
        tasksInWeek.get(0).getDailyEntries()[2].setDuration(7.7f);
        tasksInWeek.get(0).getDailyEntries()[3].setDuration(6.6f);
        tasksInWeek.get(0).getDailyEntries()[4].setDuration(5.5f);
        
        assertEquals("5 update", 5, weekEntryService.saveWeek(tasksInWeek));
        
        List<TaskInWeek> tasksInWeekReread = weekEntryService.getWorkWeek(user, dt);
        assertEquals("updated 9.9f", 9.9f, tasksInWeekReread.get(0).getDailyEntries()[0].getDuration(),0);   
        assertEquals("updated 6.6f", 6.6f, tasksInWeekReread.get(0).getDailyEntries()[3].getDuration(),0);  
    }
}
