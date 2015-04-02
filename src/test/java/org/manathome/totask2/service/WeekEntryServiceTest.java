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

        float oldValue = 0;
        LocalDate dt   = LocalDate.now();        
        User      user = userRepository.getOne(TestConstants.TEST_USER);
                
        List<TaskInWeek> tasksInWeek = weekEntryService.getWorkWeek(user, dt);
        
        int tasksCount = tasksInWeek.size();
        LOG.debug("loaded: " + tasksCount + " tasks: "  + tasksInWeek);
        assertThat("0 updates/inserts on new read", weekEntryService.saveWeek(tasksInWeek), is(0));
        assertThat("at least 5 entries", tasksInWeek.get(0).getDailyEntries().count(), greaterThan(4L));
        
        float oldValue0 = tasksInWeek.get(0).getDailyEntry(0).getDuration();
        LOG.debug("change entry 0: " + tasksInWeek.get(0).getDailyEntry(0) + " to " + oldValue0 + 1);
        tasksInWeek.get(0).setDailyEntry(0, tasksInWeek.get(0).getDailyEntry(0).setDuration(oldValue0 + 1));
        assertThat("entry 0 modified", tasksInWeek.get(0).getDailyEntry(0).isModifiedByUser(), is(true));
        
        LOG.debug("change entry 1: " + tasksInWeek.get(0).getDailyEntry(1) + " to 8.8");
        tasksInWeek.get(0).setDailyEntry(1, tasksInWeek.get(0).getDailyEntry(1).setDuration(8.8f));
        assertThat("entry 1 modified", tasksInWeek.get(0).getDailyEntry(1).isModifiedByUser(), is(true));
        
        oldValue = tasksInWeek.get(0).getDailyEntry(2).getDuration();
        LOG.debug("change entry 2: " + tasksInWeek.get(0).getDailyEntry(2) + " to " + oldValue + 1);
        tasksInWeek.get(0).setDailyEntry(2, tasksInWeek.get(0).getDailyEntry(2).setDuration(oldValue + 1));
        assertThat("entry 1 modified", tasksInWeek.get(0).getDailyEntry(2).isModifiedByUser(), is(true));

        float oldValue3 = tasksInWeek.get(0).getDailyEntry(3).getDuration();
        LOG.debug("change entry 3: " + tasksInWeek.get(0).getDailyEntry(3) + " to " + oldValue3 + 1);
        tasksInWeek.get(0).setDailyEntry(3, tasksInWeek.get(0).getDailyEntry(3).setDuration(oldValue3 + 1));
        assertThat("entry 1 modified", tasksInWeek.get(0).getDailyEntry(3).isModifiedByUser(), is(true));

        
        oldValue = tasksInWeek.get(0).getDailyEntry(4).getDuration();
        LOG.debug("change entry 4: " + tasksInWeek.get(0).getDailyEntry(4) + " to " + oldValue + 1);
        tasksInWeek.get(0).setDailyEntry(4, tasksInWeek.get(0).getDailyEntry(4).setDuration(oldValue + 1));
        assertThat("entry 4 modified", tasksInWeek.get(0).getDailyEntry(4).isModifiedByUser(), is(true));
        
        assertThat("changed 5 days on task 0: ", tasksInWeek.get(0).getDailyEntries().filter(de -> de.isModifiedByUser() || de.isNew()).count(), is(5L));
        
        assertThat("5 updated entries", weekEntryService.saveWeek(tasksInWeek), is(5));
        
        List<TaskInWeek> tasksInWeekReread = weekEntryService.getWorkWeek(user, dt);
        assertThat("updated 0 entry to " + oldValue0 + 1, tasksInWeekReread.get(0).getDailyEntry(0).getDuration(), is(oldValue0 + 1));   
        assertThat("updated 3 entry to " + oldValue3 + 1, tasksInWeekReread.get(0).getDailyEntry(3).getDuration(), is(oldValue3 + 1));  
    }
}
