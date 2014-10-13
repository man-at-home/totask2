package org.regele.totask2;

import org.regele.totask2.util.ApplicationAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * security configuration for this web application.
 * 
 * @author Manfred
 * @since 2014-10-12
 */
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(WebSecurityConfig.class);
    
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
    
    @Configuration
    protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            
            LOG.info("enabling in memory user store (init)");
            
            auth
               .inMemoryAuthentication()
               .withUser("unit-test-user").password("password").roles("USER", "ADMIN");
            
        }
    }
    
    
    /** get current signed in user. */
    public static String getCurrentUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        ApplicationAssert.assertNotNull("principal", principal);
        
        if (principal instanceof UserDetails) {
          return ((UserDetails)principal).getUsername();
        } else {
          return principal.toString();
        }
    }

}
