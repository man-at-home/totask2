package org.regele.totask2.controller;


import org.regele.totask2.model.User;
import org.regele.totask2.service.UserCachingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 
 * REST controller, used as ajax endpoint providing users data for autocompletion. 
 * (used by TaskAssignment).
 */
@RestController
public class UserController {
    
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    
    @Autowired private UserCachingService userCachingService;
    
    /** get all users the fit the given "term". */
    @Secured("ROLE_ADMIN")
    @RequestMapping("/users")
    public List<User> getUsers(@RequestParam(value = "term", defaultValue = "") final String term) {
        LOG.debug("/users, term=" + term);
        return userCachingService.getCachedUsers(term);        
    }

}