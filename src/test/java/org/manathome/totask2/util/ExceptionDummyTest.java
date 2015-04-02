package org.manathome.totask2.util;

import static org.junit.Assert.*;

import org.junit.Test;

/** dummy test case for exception (no test coverage otherwise). */
public class ExceptionDummyTest {

    @Test(expected = EnvironmentException.class)
    public void testEnvironmentException() {
        Exception ex = new EnvironmentException("x");
        assertEquals("x", ex.getMessage());
        ex = new EnvironmentException();
        throw new EnvironmentException("nested test ex", ex);
    }

    @Test(expected = ProjectNotFoundException.class)
    public void testProjectNotFoundException() {
        Exception ex = new ProjectNotFoundException("x");
        assertEquals("x", ex.getMessage());
        throw new ProjectNotFoundException();
    }    
    
    @Test(expected = UserNotFoundException.class)
    public void testUserNotFoundException() {
        Exception ex = new UserNotFoundException("xy");
        assertEquals("xy", ex.getMessage());
        throw new UserNotFoundException();
    }
    
    @Test(expected = TaskNotFoundException.class)
    public void testTaskNotFoundException() {
        Exception ex = new TaskNotFoundException("xy");
        assertEquals("xy", ex.getMessage());
        throw new TaskNotFoundException();
    }
    
    @Test(expected = NotAllowedException.class)
    public void testNotAllowedException() {    
        throw new NotAllowedException();
    }

    
    @Test(expected = TaskAssignmentNotFoundException.class)
    public void testTaskAssignmentNotFoundException() {
        Exception ex = new TaskAssignmentNotFoundException("xy");
        assertEquals("xy", ex.getMessage());
        throw new TaskAssignmentNotFoundException();
    }  
    

    @Test(expected = InvalidClientArgumentsException.class)
    public void testInvalidClientArgumentsException() {
        Exception ex = new InvalidClientArgumentsException("xy");
        assertEquals("xy", ex.getMessage());
        throw new InvalidClientArgumentsException();
    }  
    
    
}
