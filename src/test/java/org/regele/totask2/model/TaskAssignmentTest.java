package org.regele.totask2.model;

import static org.junit.Assert.*;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/** testing task assignment (without database). */
public class TaskAssignmentTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(TaskAssignmentTest.class);

    @Test
    public void testCtor() {
        User u = new User();
        Task t = new Task();
        
        TaskAssignment ta = new TaskAssignment(t, u);
        assertNotNull("toString working", ta.toString());        
        assertNull(ta.getUntil());
        assertNotNull(ta.getFrom());
        assertEquals(t, ta.getTask());
        assertEquals(u, ta.getUser());
    } 
    
    @Test
    public void testSetUntil() {
        
        TaskAssignment ta = new TaskAssignment();
        assertNotNull("toString working", ta.toString());
        assertNull(ta.getUntil());
        
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        ta.setUntil(tomorrow);
        
        assertEquals("until conversion not clean", tomorrow, ta.getUntil());
        
        LOG.debug("" + ta);
    }

}
