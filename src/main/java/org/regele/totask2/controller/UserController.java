package org.regele.totask2.controller;

import org.regele.totask2.model.User;
import org.regele.totask2.service.UserCachingService;
import org.regele.totask2.util.Authorisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import java.util.List;

import javax.ws.rs.Produces;

/** 
 * REST controller, used as ajax endpoint providing users data for autocompletion. 
 * (used by TaskAssignment).
 * 
 * @see TaskAssignmentController
 */
@RestController
@Api(value="user REST-API", description = "totask2 user REST API")
public class UserController {
    
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    
    @Autowired private UserCachingService userCachingService;
    
    
// tag::developer-manual-autocomplete-backend[]
    
    /** REST API. get all users the fit the given "term". */
    @Secured(Authorisation.ROLE_ADMIN)
    @ApiIgnore
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getUsers(@RequestParam(value = "term", defaultValue = "") final String term) {
        LOG.debug("/users, term=" + term);
        return userCachingService.getCachedUsers(term);        
    }
    
//  end::developer-manual-autocomplete-backend[]

    /** REST API. get all users the fit the given "term". */
    @RequestMapping(value = "REST/users", method = RequestMethod.GET)
    @Produces("application/json")
    @ApiOperation(value = "REST/users", notes = "return all known users given a search criteria *term*")
    @ApiResponses(value = { @ApiResponse( code = 200, message = "ok") })
    public List<User> restUsers(
            @ApiParam(value = "optional search term", required = false, defaultValue="", name="term")  
            @RequestParam(value = "term", defaultValue = "", required = false) 
            final String term
            ) 
    {
        LOG.debug("/REST/users, term=" + term);
        return userCachingService.getCachedUsers(term);        
    }

    
}
