package org.regele.totask2.model;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.regele.totask2.Application;
import org.regele.totask2.util.LocalDateConverter;
import org.regele.totask2.util.TestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.wordnik.swagger.config.SwaggerConfig;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;


/** testing db access. */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, SwaggerConfig.class })
@WebAppConfiguration
public class WorkEntryRepositoryTest {
    

    private static final Logger LOG = LoggerFactory.getLogger(WorkEntryRepositoryTest.class);
    
    /** work entry repository under test. */
    @Autowired
    private WorkEntryRepository workEntryRepository;
    
    @Autowired private UserRepository userRepository;
    @Autowired private TaskRepository taskRepository;
    
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LOG.debug("testing db workentry access ");
    }

    /** workEntryyRepository.count. */
    @Test   
    public void testReadWorkEntry() {
        assertNotNull("work entry repo not injected", workEntryRepository);
        assertTrue("work entryy table access not possible" ,  workEntryRepository.count() >= 0);
    }

    /** save, getOne. */
    @Test   
    public void testStoreEntry() {
        
        User user = userRepository.getOne(TestConstants.ADMIN_USER);
        Task task = taskRepository.getOne(1L);
 
        WorkEntry workEntry = new WorkEntry(user, task);
        workEntry.setDuration(3);
        workEntry.setComment("junit store");
        workEntry.setAtDate(LocalDate.now());
        
        WorkEntry savedEntry = workEntryRepository.save(workEntry);
        LOG.debug("Saved new workentry " + savedEntry + " id:" + savedEntry.getId());
        
        assertTrue("id for entry generated" , savedEntry.getId() > 0);
        assertTrue("new work entry id exists", workEntryRepository.exists(savedEntry.getId()));
        
        WorkEntry refetchedEntry = workEntryRepository.findOne(savedEntry.getId());
        assertNotNull("entry found", refetchedEntry);
        assertEquals("duration", 3, refetchedEntry.getDuration(), 0);
        assertEquals("comment", "junit store" , refetchedEntry.getComment());
        assertEquals("at", LocalDate.now() , refetchedEntry.getAtDate()); // does not work on midnight ;-)
    }
    
    /** save, delete, findOne. */
    @Test   
    public void testStoreAndDeleteUser() {
        User user = userRepository.getOne(TestConstants.ADMIN_USER);
        Task task = taskRepository.getOne(1L);
 
        WorkEntry workEntry = new WorkEntry(user, task);
        workEntry.setDuration(4);
        workEntry.setComment("junit store to delete");        
        WorkEntry savedEntry = workEntryRepository.save(workEntry);
        long id = savedEntry.getId();
        
        workEntryRepository.delete(savedEntry);
        WorkEntry notToBeFoundEntry = workEntryRepository.findOne(id);
        assertNull("deleted entry found", notToBeFoundEntry);
    }

    
    /** find admin user inserted by data.sql. */
    @Test   
    public void testReadTestEntries() {

        WorkEntry entry  = workEntryRepository.findOne(1L);
        assertNotNull("entry 1 found" , entry);
        
        Date dt = LocalDateConverter.toDate(LocalDate.now());
        LOG.debug("retrieving entries for user 2L and date " + dt);
        List<WorkEntry> entries = workEntryRepository.findForUserAndDay(TestConstants.TEST_USER, dt);
        
        entries.stream().forEach( e -> LOG.debug("entry: " + e));
        
        assertTrue("not enough entries found: " + entries.size(), entries.size() >= 2);
        
        assertTrue(" entry 1 found", entries.stream().anyMatch( we -> we.getId() == 1L && we.getComment().equals("entry 4.0 dev") ));
        assertTrue(" entry 10 found", entries.stream().anyMatch( we -> we.getId() == 10L && we.getComment().equals("entry 5.0 test") ));        
    }   
    
}

