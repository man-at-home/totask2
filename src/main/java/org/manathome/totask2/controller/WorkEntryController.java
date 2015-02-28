package org.manathome.totask2.controller;

import org.manathome.totask2.model.TaskInWeek;
import org.manathome.totask2.model.User;
import org.manathome.totask2.model.UserRepository;
import org.manathome.totask2.model.WorkEntry;
import org.manathome.totask2.service.UserDetailsServiceImpl;
import org.manathome.totask2.service.WeekEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Produces;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


/** 
 * REST controller providing work data for one day, will be used by totask2.mobile app. 
 * 
 * @author man-at-home
 * @since  2015-02-28
 * @see    WorkEntry 
 */
@RestController
@Api(value = "workEntry REST-API", description = "totask2 workEntry REST API")
public class WorkEntryController {
    
    private static final Logger LOG = LoggerFactory.getLogger(WorkEntryController.class);
    
    @Autowired private WeekEntryService weekEntryService;    
    @Autowired private UserRepository   userRepository;
    
    public WorkEntryController() {}

    /** REST API. get all workEntries for user and given day. */
    @RequestMapping(value = "REST/workEntries/{day}", method = RequestMethod.GET)
    @Produces("application/json")
    @ApiOperation(value = "REST/workEntries", notes = "return all workentries for user and day")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "ok") })
    public List<WorkEntry> restWorkEntries(
            @ApiParam(value = "day (selection criteria)", required = true, name = "day")  
            @PathVariable(value = "day") 
            final String day
            ) {
        
        LOG.debug("/REST/workEntries, day=" + day);
        
        LocalDate dt = LocalDate.parse(day);
        
        User user = userRepository
                    .findByUserName(UserDetailsServiceImpl.getCurrentUserName())
                    .stream().findFirst().get(); 
                                             
        List<TaskInWeek> tasksInWeek = weekEntryService.getWorkWeek(user, dt);
        LOG.debug("Tasks in week " + dt + ": " + tasksInWeek.size());
                
        List<WorkEntry> wes = tasksInWeek
                .stream()
                .flatMap(tiw -> Arrays.stream(tiw.getDailyEntries())
                                      .filter(we -> we.getAtDate().isEqual(dt))   // only of correct day
                        )
                .collect(Collectors.toList());
                
        LOG.debug("workEntries at day " + dt + ": " + wes.size());
        wes.forEach(we -> LOG.debug("retuned workentry: " + we));
        
        return wes;
    }
}
