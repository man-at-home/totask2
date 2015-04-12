package org.manathome.totask2;

import org.manathome.totask2.model.ProjectRepository;
import org.manathome.totask2.util.Totask2Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/** 
 * spring actuator health indicator. 
 * 
 * output of additional information on monitor/health REST endpoint.
 * 
 * @author man-at-home
 * @since  2015-03-09
 *
 */
@Component
public class ApplicationHealthIndicator extends AbstractHealthIndicator {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationHealthIndicator.class);

    @Autowired private ProjectRepository projectRepository;
    @Autowired private Environment    env; 
    
    /** health: access to database + environment. */
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        
        LOG.trace("doHealthCheck");
        
        builder.withDetail("projectsInDatabase", projectRepository.count());
        builder.withDetail("Environment.TOTASK2_MODE", env.getProperty(Totask2Constants.ENV_TOTASK2_MODE, "UNKNOWN"));
        builder.withDetail("Environment.DOGSTATSD_PORT", env.getProperty(Totask2Constants.ENV_OPENSHIFT_DATADOG_DOGSTATSD_PORT, "not set"));
        builder.withDetail("Environment.KEEN_PROJECT", env.getProperty(Totask2Constants.ENV_KEEN_PROJECT_ID, "not set"));
        builder.up();  // database query on projects was run, thats enough for this check.
    }    

}
