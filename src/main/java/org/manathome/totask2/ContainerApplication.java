package org.manathome.totask2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * spring boot configuration for deployment on appserver (wildfly).
 * 
 * @see <a href="http://www.wildfly.com">wildfly (red hat app server)</a>
 * @author man-at-home
 * @since  2014-11-29
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class ContainerApplication extends SpringBootServletInitializer { 
    
    private static final Logger LOG = LoggerFactory.getLogger(ContainerApplication.class);

    /** start standalone. */
    public static void main(String[] args) {
        LOG.info("starting application (ContainerApplciation)");
        
        SpringApplication app = new SpringApplication(Application.class);
        app.setShowBanner(false);
        app.run(args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }    
}
