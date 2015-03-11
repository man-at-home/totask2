package org.manathome.totask2;

import org.manathome.totask2.model.User;
import org.manathome.totask2.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;


/**
 * security configuration for the totask2 web application.
 * 
 * @see UserDetailsServiceImpl
 * @see <a href="http://projects.spring.io/spring-security/">http://projects.spring.io/spring-security/</a>
 * 
 * @author man-at-home
 * @since 2014-10-12
 */
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired private UserDetailsServiceImpl userDetailsServiceImpl;
    
    
    /** secure most pages. Exceptions are index.html and javascript / css resources.  */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        LOG.info("WEB: enabling security (configure form based login)");
        
        http
            .authorizeRequests()
                .antMatchers("/", "/index.html", "/js/*", "/css/*", "/bootstrap/**/*", "/images/*").permitAll()
                .anyRequest().authenticated();
        http
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
        
        http.csrf().disable();  // w/ unit-tests, waiting on spring-security-test!.
        
        http
            .exceptionHandling()
            .accessDeniedPage("/403");
    }  
    
    
    /** use own user implementation. */
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        
        LOG.debug("WEB: configuring userDetailsService " + userDetailsServiceImpl);
        auth.userDetailsService(userDetailsServiceImpl)
            .passwordEncoder(User.getPasswordEncoder())
            ;  
    }
    
    
    /** second configuration: using basic authentication for totask2.mobile's REST API. */
    @Configuration
    @Order(1) 
    public static class RestApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        
        @Autowired private UserDetailsServiceImpl userDetailsServiceImpl;        
        
        /** use basic authentication for /APP/REST URLs. */
        protected void configure(HttpSecurity http) throws Exception {
            
            LOG.info("WEB/REST-API: enabling security (configure basic auth)");
            
            http
                .antMatcher("/APP/REST/**")
                .authorizeRequests()
                .antMatchers("/APP/REST/**").hasRole("USER")
                .and()
                .httpBasic();

            http.csrf().disable();  // w/ rest posts APP/REST/WorkEntry
        }
        
        /** use same users as web application. */
        @Override
        protected void configure(AuthenticationManagerBuilder auth)
                throws Exception {
            
            LOG.debug("WEB/REST-API: configuring userDetailsService " + userDetailsServiceImpl);
            auth.userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(User.getPasswordEncoder())
                ;
        }
    }//static inner class
   
}//class
