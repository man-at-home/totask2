package org.manathome.totask2.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;



/**
 * testing the project class.
 * @author man-at-home
 *
 */
public class ProjectTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProjectTest.class);

    /**
     * Test method for {@link org.manathome.totask2.model.Project#setName(java.lang.String)}.
     */
    @Test
    public final void testNameValidation() {
        
        Project p = new Project();
        p.setName("1");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Project>> violations = validator.validate(p);
   
        violations.stream().forEach(v -> LOG.debug("Violation: " + v.getMessage() + "/" + v.getPropertyPath() + "/" + v.getInvalidValue()));
               
        assertTrue("validations did not fail", violations.size() > 0);
        assertTrue("validation for name=1 not failed", 
                violations.stream().anyMatch(v -> v.getInvalidValue().equals("1") && v.getPropertyPath().toString().equals("name")));
        
   }

}
