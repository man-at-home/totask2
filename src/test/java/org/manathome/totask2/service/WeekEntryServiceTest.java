package org.manathome.totask2.service;

import static org.hamcrest.Matchers.*;
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
        
        LOG.debug("loaded: " + tasksInWeek);
        assertThat("0 updates/inserts on new read", weekEntryService.saveWeek(tasksInWeek), is(0));
        LOG.debug("change entry: " + tasksInWeek.get(0).getDailyEntry(0) + " to 9.9");
        tasksInWeek.get(0).setDailyEntry(0, tasksInWeek.get(0).getDailyEntry(0).setDuration(0.8f));
        LOG.debug("change entry: " + tasksInWeek.get(0).getDailyEntry(1) + " to 8.8");
        tasksInWeek.get(0).setDailyEntry(1, tasksInWeek.get(0).getDailyEntry(1).setDuration(1.7f));
        LOG.debug("change entry: " + tasksInWeek.get(0).getDailyEntry(2) + " to 7.7");
        tasksInWeek.get(0).setDailyEntry(2, tasksInWeek.get(0).getDailyEntry(2).setDuration(2.6f));
        LOG.debug("change entry: " + tasksInWeek.get(0).getDailyEntry(3) + " to 6.6");
        tasksInWeek.get(0).setDailyEntry(3, tasksInWeek.get(0).getDailyEntry(3).setDuration(3.5f));
        LOG.debug("change entry: " + tasksInWeek.get(0).getDailyEntry(4) + " to 5.5");
        tasksInWeek.get(0).setDailyEntry(4, tasksInWeek.get(0).getDailyEntry(4).setDuration(4.4f));
        
        assertThat("5 changed durations", tasksInWeek.get(0).getDailyEntries().filter(de -> de.isModifiedByUser() || de.isNew()).count(), is(5L));
        
        assertThat("5 updated entries", weekEntryService.saveWeek(tasksInWeek), is(5));
        
        List<TaskInWeek> tasksInWeekReread = weekEntryService.getWorkWeek(user, dt);
        assertThat("updated to 0.8f", tasksInWeekReread.get(0).getDailyEntry(0).getDuration(), is(0.8f));   
        assertThat("updated to 3.5f", tasksInWeekReread.get(0).getDailyEntry(3).getDuration(), is(3.5f));  
    }
}
