package org.regele.totask2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.context.annotation.ComponentScan;

/** application starter. */
@ComponentScan
@EnableAutoConfiguration
public class Application {
    
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        LOG.debug("starting application");
        SpringApplication.run(Application.class, args);
    }
    
    
    public static String getInfo() {
        return "manfreds test spring project";
    }
    
}
