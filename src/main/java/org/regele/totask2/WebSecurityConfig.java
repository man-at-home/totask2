package org.regele.totask2;

import org.regele.totask2.model.User;
import org.regele.totask2.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;


/**
 * security configuration for this web application.
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
        
        LOG.info("enabling security (configure)");
        
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
        
        http
            .exceptionHandling()
            .accessDeniedPage("/403");
    }  
    
    
    /** use own user implementation. */
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        
        LOG.debug("configuring userDetailsService " + userDetailsServiceImpl);
        auth.userDetailsService(userDetailsServiceImpl)
            .passwordEncoder(User.getPasswordEncoder())
            ;
       
    }
    
    

   
}
