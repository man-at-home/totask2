package org.regele.totask2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.regele.totask2.model.User;
import org.regele.totask2.model.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * user caching.
 * 
 * @see User
 * 
 * @author Manfred
 * @since  2014-10-28
 */
@Service
public class UserCachingService {

    private static final Logger LOG = LoggerFactory.getLogger(UserCachingService.class); 

    /** user. */
    @Autowired private UserRepository userRepository;      
    
    /** cached user access. */
    @Cacheable("users")
    public List<User> getCachedUsers() {
        List<User> users = userRepository.findAll();
        LOG.debug("getCachedUsers() -> " + users.size() + " hits.");
        return users;
    }   
    
    public List<User> getCachedUsers(final String filter) {
        List<User> users;
        if( filter == null || filter.length() == 0) {
            users = this.getCachedUsers();
        }
        else {
            users = this.getCachedUsers()
                    .stream()
                    .filter( u -> u.getUsername().indexOf(filter) >= 0 || u.getDisplayName().indexOf(filter) >= 0)   
                    .collect(Collectors.toCollection(ArrayList::new))
                   ;
        }
        
        LOG.debug("getCachedUsers(" + filter + ") -> " + users.size() + " hits.");
        return users;        
    }   

}
