package org.manathome.totask2.service;


import org.manathome.totask2.model.User;
import org.manathome.totask2.model.UserRepository;
import org.manathome.totask2.util.ApplicationAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * connect spring-security with home grown tt_user/user of totask2.
 * 
 * @see User
 * @see <a href="http://projects.spring.io/spring-security/">http://projects.spring.io/spring-security/</a>
 * 
 * @author man-at-home
 * @since  2014-10-25
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private static final Logger LOG = LoggerFactory.getLogger(UserDetailsServiceImpl.class);    
    

    /** user. */
    @Autowired private UserRepository userRepository;    
    
    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String userName)
            throws UsernameNotFoundException {
        
        List<User> users = userRepository.findByUserName(userName);
        if (users.size() != 1) {
            LOG.info("user <" + userName + "> not found: " + users.size());
            throw new UsernameNotFoundException(userName + " no found.");
        } else {
            LOG.debug("found user: " + users.get(0) + " with pw: " + users.get(0).getPassword());
        }
 
        return users.get(0);
    }
        
    
    public static String getCurrentUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        ApplicationAssert.assertNotNull("principal", principal);
        
        if (principal instanceof UserDetails) {
          return ((UserDetails) principal).getUsername();
        } else {
          return principal.toString();
        }
    } 

}
