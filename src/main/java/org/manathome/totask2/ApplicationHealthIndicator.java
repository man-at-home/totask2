package org.manathome.totask2;

import org.manathome.totask2.model.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/** 
 * spring actuator health indicator. 
 * 
 * output of additional information on /health endpoint.
 * 
 * @author man-at-home
 * @since  2015-03-09
 *
 */
@Component
public class ApplicationHealthIndicator extends AbstractHealthIndicator {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationHealthIndicator.class);

    @Autowired private ProjectRepository projectRepository;
    
    /** health: access to database. */
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        
        LOG.trace("doHealthCheck");
        
        builder.withDetail("projectsInDatabase", projectRepository.count());
        builder.up();
    }    

}
