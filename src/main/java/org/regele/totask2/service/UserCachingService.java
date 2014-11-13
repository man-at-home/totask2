package org.regele.totask2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.regele.totask2.model.User;
import org.regele.totask2.model.UserRepository;
import org.regele.totask2.util.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * basic {@link User} caching, avoiding endless db access.
 * 
 * @see User 
 * @see #getCachedUsers()
 * 
 * @author man-at-home
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
    
    /** find user by user.id. */
    public User getCachedUserById(final long userId) {
        
        return this.getCachedUsers()
                .stream()
                .filter( u -> u.getId() == userId)
                .findFirst()
                .orElseThrow( () -> new UserNotFoundException("no user with id " + userId))
                ;
    }

    /** find user by logged in user */
    public User getCachedUser(final Authentication authentication) {
        
        if( authentication == null)
            throw new NullPointerException("no authentication object");
        
        if( authentication.getPrincipal() instanceof User)
            return (User) authentication.getPrincipal(); 
        
        return this.getCachedUsers()
                .stream()
                .filter( u -> u.getUsername() == authentication.getName())
                .findFirst()
                .orElseThrow( () -> new UserNotFoundException("no user with id " + authentication.getName()))
                ;
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
