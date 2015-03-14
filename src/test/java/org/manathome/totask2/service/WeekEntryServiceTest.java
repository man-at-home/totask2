package org.manathome.totask2.service;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.manathome.totask2.Application;
import org.manathome.totask2.SwaggerConfig;
import org.manathome.totask2.model.TaskInWeek;
import org.manathome.totask2.model.User;
import org.manathome.totask2.model.UserRepository;
import org.manathome.totask2.model.WorkEntryRepository;
import org.manathome.totask2.util.TestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
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
        User      user = userRepository.getOne(TestConstants.TEST_USER);
        
        List<TaskInWeek> tasksInWeek = weekEntryService.getWorkWeek(user, dt);
        
        assertNotNull("TaskInWeek found" , tasksInWeek);
        
        LOG.debug("retrieved tasks for week: " + tasksInWeek.size());
        
        tasksInWeek.forEach(tiw -> LOG.debug(tiw.getTask() + ": " + tiw.getDuration() + " hours."));
        
        for (TaskInWeek tiw : tasksInWeek) {
            
           assertNotNull(tiw.getTask());
           assertNotNull(tiw.getDailyEntries());
           assertEquals("7 workdays for task", 7, tiw.getDailyEntries().count());
           assertFalse("some entries null", tiw.getDailyEntries().anyMatch(we -> we == null));
           assertFalse("some entries.task null", tiw.getDailyEntries().anyMatch(we -> we.getTask() == null));
           assertFalse("some entries.user null", tiw.getDailyEntries().anyMatch(we -> we.getUser() == null));
           
           LOG.debug("work entry " + tiw.getTask() + " Mo: " + tiw.getDailyEntry(0) + ", Di: " + tiw.getDailyEntry(1));
        }
    }   
    
    
    /** update. */
    @Test   
    public void testUpdateTestEntries() {

        LocalDate dt   = LocalDate.now();        
        User      user = userRepository.getOne(TestConstants.TEST_USER);
                
        List<TaskInWeek> tasksInWeek = weekEntryService.getWorkWeek(user, dt);
        assertEquals("0 update", 0, weekEntryService.saveWeek(tasksInWeek));
        
        tasksInWeek.get(0).setDailyEntry(0, tasksInWeek.get(0).getDailyEntry(0).setDuration(9.9f));
        tasksInWeek.get(0).setDailyEntry(1, tasksInWeek.get(0).getDailyEntry(1).setDuration(8.8f));
        tasksInWeek.get(0).setDailyEntry(2, tasksInWeek.get(0).getDailyEntry(2).setDuration(7.7f));
        tasksInWeek.get(0).setDailyEntry(3, tasksInWeek.get(0).getDailyEntry(3).setDuration(6.6f));
        tasksInWeek.get(0).setDailyEntry(4, tasksInWeek.get(0).getDailyEntry(4).setDuration(5.5f));
        
        assertEquals("5 update", 5, weekEntryService.saveWeek(tasksInWeek));
        
        List<TaskInWeek> tasksInWeekReread = weekEntryService.getWorkWeek(user, dt);
        assertEquals("updated 9.9f", 9.9f, tasksInWeekReread.get(0).getDailyEntry(0).getDuration(), 0);   
        assertEquals("updated 6.6f", 6.6f, tasksInWeekReread.get(0).getDailyEntry(3).getDuration(), 0);  
    }
}
