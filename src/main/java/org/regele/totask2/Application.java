package org.regele.totask2;

import org.regele.totask2.controller.AppController;
import org.regele.totask2.service.UserCachingService;
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

import com.mangofactory.swagger.plugin.EnableSwagger;

/** 
 * totask2 application starter (spring-boot web application).
 * 
 * @see AppController
 * @see <a href="https://github.com/man-at-home/totask2">https://github.com/man-at-home/totask2</a>
 * 
 * @author man-at-home 
 * @since 2014
 */
@Configuration
@ComponentScan
@EnableJpaRepositories
@EnableAutoConfiguration
@EnableCaching
@EnableSwagger
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
    
    /** caching user list.
     * 
     * @see UserCachingService
     * @see User 
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("users");
    }
    
    /** dummy message. */
    public static String getInfo() {
        return "man-at-homes test spring project";
    }
    
}
