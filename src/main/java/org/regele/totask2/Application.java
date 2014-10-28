package org.regele.totask2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/** 
 * totask2 application starter.
 * @author Manfred 
 */
@Configuration
@ComponentScan
@EnableJpaRepositories
@EnableAutoConfiguration
@EnableCaching
public class Application  extends WebMvcConfigurerAdapter  {
    
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    /** start web application stand alone. */
    public static void main(String[] args) {
        LOG.debug("starting application");
        
        SpringApplication app = new SpringApplication(Application.class);
        app.setShowBanner(false);
        app.run(args);
    }
    
    /** security: show where the login page is. */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
    
    /** caching user list. */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("users");
    }
    
    /** dummy message. */
    public static String getInfo() {
        return "manfreds test spring project";
    }
    
}
