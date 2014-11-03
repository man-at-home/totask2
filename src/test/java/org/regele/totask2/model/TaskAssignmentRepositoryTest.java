package org.regele.totask2.model;

import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

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

/** testing db access. */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
public class TaskAssignmentRepositoryTest {

   private static final Logger LOG = LoggerFactory.getLogger(TaskAssignmentRepositoryTest.class);
    
    /** taskAssignment repository under test. */
    @Autowired
    private TaskAssignmentRepository taskAssignmentRepository;
      
    @Autowired private UserRepository userRepository;
    @Autowired private TaskRepository taskRepository;
    
    @Test
    public void testStoreRetrieveDelete() {
        
        User user = userRepository.getOne(TestConstants.ADMIN_USER);
        Task task = taskRepository.getOne(1L);

        // store
        
        TaskAssignment ta = task.addAssignment(user);
        
        TaskAssignment taStored = taskAssignmentRepository.saveAndFlush(ta);
        assertTrue("pk id missing", taStored.getId() > 0);
 
        // retrieve
        
        TaskAssignment taRefetched = taskAssignmentRepository.findOne( taStored.getId());
        assertNotNull("entry found", taRefetched);
        assertEquals("taRefetched user", user.getId(), taRefetched.getUser().getId());       
        assertEquals("taRefetched task", task.getId(), taRefetched.getTask().getId());
        
        Task taskRefetched = taskRepository.findOne(task.getId());
        assertTrue("task misses assignment" , taskRefetched.getAssignments().anyMatch( a -> a.getId() == taRefetched.getId()));
        
        //delete
        taskAssignmentRepository.delete(taRefetched);
    }

    /** custom finder. */
    @Test
    public void testFindAdminAssignments() {
        LocalDate from = LocalDate.now().minusYears(1);
        LocalDate until = LocalDate.now().plusDays(7);
        List<TaskAssignment> tas = taskAssignmentRepository.findByUserAndPeriod(TestConstants.ADMIN_USER, LocalDateConverter.toDate(from), LocalDateConverter.toDate(until));
        assertNotNull(tas);
        tas.stream().forEach( a -> LOG.debug("found assignment: " + a));
        assertEquals("assignments unexpected", 2, tas.size());
    }
    
    /** custom finder. */
    @Test
    public void testFindByUserAssignmentsThisWeek() {
        
        LocalDate date  = LocalDate.now().with(previousOrSame(DayOfWeek.MONDAY));        
        Date from  = LocalDateConverter.toDate(date);
        Date until = LocalDateConverter.toDate(date.with(nextOrSame(DayOfWeek.SUNDAY)));
        
        LOG.debug("find current for Testuser: " + from + " - " + until);
        List<TaskAssignment> tas = taskAssignmentRepository.findByUserAndPeriod(TestConstants.TEST_USER, from, until);
        assertNotNull(tas);        
        tas.stream().forEach( a -> LOG.debug("found assignment: " + a));
        assertEquals("assignment count unexpected", 3, tas.size());
    }    

    /** custom finder. */
    @Test
    public void testFindByUserAssignmentsFutureWeek() {
        
        LocalDate date  = LocalDate.now().plusMonths(2).with(previousOrSame(DayOfWeek.MONDAY));        
        Date from  = LocalDateConverter.toDate(date);
        Date until = LocalDateConverter.toDate(date.with(nextOrSame(DayOfWeek.SUNDAY)));
        
        LOG.debug("find future for Testuser: " + from + " - " + until);
        List<TaskAssignment> tas = taskAssignmentRepository.findByUserAndPeriod(TestConstants.TEST_USER, from, until);
        assertNotNull(tas);        
        tas.stream().forEach( a -> LOG.debug("found assignment: " + a));
        assertEquals("assignment count unexpected", 2, tas.size());
    }     

    @Test
    public void testFindAll() {
        assertNotNull("rep not autowired" , taskAssignmentRepository);
        List<TaskAssignment> tas = this.taskAssignmentRepository.findAll();
        
        assertTrue("not task assignments found", tas.size() > 0);
        LOG.debug("taskAssgnments table exists");
    }

}
