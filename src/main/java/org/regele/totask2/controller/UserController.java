package org.regele.totask2.controller;

import java.util.List;

import org.regele.totask2.model.User;
import org.regele.totask2.service.UserCachingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** ajax endpoint providing users. */
@RestController
public class UserController {
    
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    
    @Autowired private UserCachingService userCachingService;
    
    @RequestMapping("/users")
    public List<User> getUsers(@RequestParam(value="filter", defaultValue="") final String filter) {
        LOG.debug("/users, filter=" + filter);
        return userCachingService.getCachedUsers(filter);        
    }
        

}
