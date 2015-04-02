package org.manathome.totask2;


import org.manathome.totask2.controller.AppController;
import org.manathome.totask2.service.UserCachingService;
import org.manathome.totask2.util.LoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;



import java.text.SimpleDateFormat;

import javax.servlet.Filter;

import com.mangofactory.swagger.plugin.EnableSwagger;

import io.keen.client.java.JavaKeenClientBuilder;
import io.keen.client.java.KeenClient;


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

    @Autowired private Environment env;   
    
    /** security: show where the login page is. */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
    
    /** 
     * caching user list.
     * 
     * @see UserCachingService
     * @see org.manathome.totask2.model.User
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("users");
    }
    
    /** enhanced logging information. */
    @Bean
    public Filter getLoggingFilter() {
       return new LoggingFilter();
    }
    
    
    /** dummy message. */
    public static String getInfo() {
        LOG.debug("getInfo");
        return "man-at-homes test spring project";
    }
    
    
    /** used serializing json from WorkEntry.getAt instances. */
    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return builder;
    }
    
 
    /** 
     * keen.io analytics client. 
     * 
     * @see <a href="https://keen.io">keen.io</a>
     */
    @Bean
    @Description("keen.io analytics client")
    public KeenClient  getKeenClient() {
        
        /** access to environment variables. with keen.io keys. */
        String pkey = env.getProperty("KEEN_PROJECT_ID");
        String wkey = env.getProperty("KEEN_WRITE_KEY");

        if (pkey == null || wkey == null || pkey.length() == 0 || wkey.length() == 0) {
            LOG.error("keenClient: KEEN_PROJECT_ID or KEEN_WRITE_KEY not set, no analytical data will be gathered.");
        }

        KeenClient client = new JavaKeenClientBuilder().build();
        LOG.info("creating keen sink: " + client.getDefaultProject());
        return client;
    }
}
