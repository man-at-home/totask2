package org.manathome.totask2;

import static org.ajar.swaggermvcui.SwaggerSpringMvcUi.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;


/** 
 * /REST/* and /APP/REST/.* URLs provided by swapper configuration. 
 * 
 * @see <a href="http://swagger.io/">http://swagger.io/</a>
 * @see <a href="http://localhost:8080/sdoc.jsp>http://localhost:8080/sdoc.jsp</a>
 * 
 * @author man-at-home
 */
@Configuration
@EnableSwagger
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(SwaggerConfig.class);
    
    @Autowired private SpringSwaggerConfig springSwaggerConfig;
    
    /** ctor. */
    public SwaggerConfig() {
        LOG.trace("creating swagger config");
    }
   

    /** configure api urls. */
    @Bean //Don't forget the @Bean annotation
    public SwaggerSpringMvcPlugin customImplementation(){
       return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
             .apiInfo(apiInfo())
             .swaggerGroup("totask2")
             .includePatterns("/REST/.*", "/APP/REST/.*");
    }

     private ApiInfo apiInfo() {
       ApiInfo apiInfo = new ApiInfo(
               "totask2 API REST",
               "totask2 API REST acess to time logging data, swagger based",
               "totask2 API GNU GENERAL PUBLIC LICENSE",
               "totask2 API mat-at-work at github",
               "totask2 API some rights reserved",
               "https://raw.githubusercontent.com/man-at-home/totask2/master/LICENSE"
         );
       return apiInfo;
     }   

     // starting from here: swagger ui. */
     
     /** ui. */
     @Override
     public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(WEB_JAR_RESOURCE_PATTERNS)
                .addResourceLocations(WEB_JAR_RESOURCE_LOCATION)
                .setCachePeriod(0);
     }

     /** ui. */
     @Bean
     public InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix(WEB_JAR_VIEW_RESOLVER_PREFIX);
        resolver.setSuffix(WEB_JAR_VIEW_RESOLVER_SUFFIX);
        LOG.trace("created jsp view resolver for swagger " + resolver);
        return resolver;
     }

     /** ui. */
     @Override
     public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
     }
     
}
