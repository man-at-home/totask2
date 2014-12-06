package org.regele.totask2.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/** 
 * Task Tests. 
 * 
 * @see     Task
 * @author  man-at-home
 * */
public class TaskTest {

    private static final Logger LOG = LoggerFactory.getLogger(TaskTest.class);
        
    @Test
    public void testAddAssignment() {
        Task task = new Task();
        task.setId(4);
        User user = new User();
        user.setId(3);
        
       TaskAssignment ta = task.addAssignment(user);
       assertNotNull(ta);
       
       ta.setFrom(LocalDate.now());
       ta.setUntil(LocalDate.now().plusDays(3));
       
       assertEquals(4, ta.getTask().getId());
       assertEquals(3, ta.getUser().getId());
       assertTrue(task.getAssignments().anyMatch(a -> a.equals(ta)));
       
    }

    @Test
    public void testTask() {
        Project p = new Project();
        p.setId(1);
        p.setName("pname");
        Task task = new Task(p);
        task.setId(4);
        task.setName("unit task");
        LOG.debug("task: " + task);
        assertEquals(0, task.getAssignments().count());
    }

}
