package org.manathome.totask2.controller;

import org.manathome.totask2.model.Task;
import org.manathome.totask2.model.TaskInWeek;
import org.manathome.totask2.model.TaskRepository;
import org.manathome.totask2.model.User;
import org.manathome.totask2.model.UserRepository;
import org.manathome.totask2.model.WorkEntry;
import org.manathome.totask2.model.WorkEntryRepository;
import org.manathome.totask2.model.WorkEntryTransfer;
import org.manathome.totask2.service.UserDetailsServiceImpl;
import org.manathome.totask2.service.WeekEntryService;
import org.manathome.totask2.util.InvalidClientArgumentsException;
import org.manathome.totask2.util.LocalDateConverter;
import org.manathome.totask2.util.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


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
    
    @Autowired private CounterService       counterService;
    @Autowired private WeekEntryService     weekEntryService;    

    @Autowired private UserRepository       userRepository;
    @Autowired private TaskRepository       taskRepository;
    @Autowired private WorkEntryRepository  workEntryRepository;
    
    /** REST API APP/REST/workEntry POST, insert / update WorkEntry.
     * 
     *  hint: http.csrf().disable(); required.
     *  
     *  @param entryToSave     entry to save, sent from mobile app.
     *  @return                saved entry (including workEntry.id
     *  
     *  @see WorkEntry         saved data
     *  @see WorkEntryTransfer tranfered data
     */
    @RequestMapping(value = "APP/REST/workEntry", method = RequestMethod.POST)
    @ApiOperation(value = "APP/REST/workEntry", notes = "insert or update one workentry")
    public WorkEntry restSave
    (
            @ApiParam(value = "new or updated work entry data", required = true, name = "entryToSave")
            @Validated 
            @RequestBody
            final WorkEntryTransfer entryToSave
    ) {     
        LOG.trace("/APP/REST/workEntry(" + entryToSave + ") POST");
        
        User user = userRepository
                .findByUserName(UserDetailsServiceImpl.getCurrentUserName())
                .stream().findFirst().get(); 
       
        WorkEntry entry = null;
        
        if (entryToSave.getId() <= 0) {
            // new
            LOG.debug("insert new WorkEntry(" + entryToSave.getId() + "): " + entryToSave);
            Task task = taskRepository.findOne(entryToSave.getTaskId());
            if (task == null) {
                throw new TaskNotFoundException("task for entry(" + entryToSave.getId() + " not found, unknown id: " + entryToSave.getTaskId());
            }

            entry = new WorkEntry();
            entry.setUser(user);
            entry.setTask(task);
            counterService.increment("TOTASK2XX.controller.workEntry.REST.created");
            
        } else {
            // update
            LOG.debug("updating WorkEntry(" + entryToSave.getId() + "): " + entryToSave);
            entry = workEntryRepository.findOne(entryToSave.getId());
            
            if (entryToSave.getTaskId() != entry.getTaskId()) {
                throw new InvalidClientArgumentsException("task for entry(" + entryToSave.getId() + " must not change, is: " + entry.getTaskId());
            }            
            counterService.increment("TOTASK2XX.controller.workEntry.REST.changed");
        }
        
        entry.setDuration(entryToSave.getDuration());
        entry.setAtDate(LocalDateConverter.toLocalDate(entryToSave.getAt()));
        entry.setComment(entryToSave.getComment());
        
        workEntryRepository.save(entry);
        LOG.debug("saved and returning workEntry: " + entry);
        
        return entry;
    }   
    
    
    /** REST API. get all workEntries for user and current day (including dummy entries for not yet logged ones).
     * 
     * @return List of WorkEntries of all possible work entries (created and potentially created, later hav id of 0).
     */ 
    @RequestMapping(value = "APP/REST/workEntries", method = RequestMethod.GET)
    @ApiOperation(value = "APP/REST/workEntries", notes = "return all workentries for user on current day", httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "ok") })
    public List<WorkEntry> restWorkEntries() {
        
        LOG.trace("APP/REST/workEntries, today");
        
        User user = userRepository
                    .findByUserName(UserDetailsServiceImpl.getCurrentUserName())
                    .stream().findFirst().get(); 
                                             
        return getWorkEntries(user, LocalDate.now());        
    }
    

    /** REST API. get all workEntries for user and given day (including dummy entries for not yet logged ones).
     * 
     * @param day date, formatted YYYY-MM-DD.
     * @return List of WorkEntries of all possible work entries (created and potentially created, later hav id of 0).
     */ 
    @RequestMapping(value = "APP/REST/workEntries/{day}", method = RequestMethod.GET)
    @ApiOperation(value = "APP/REST/workEntries", notes = "return all workentries for user and day", httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "ok") })
    public List<WorkEntry> restWorkEntries(
            @ApiParam(value = "day (selection criteria)", required = true, name = "day")  
            @PathVariable(value = "day") 
            final String day
            ) {
        
        LOG.trace("APP/REST/workEntries, day=" + day);
        
        LocalDate dt = LocalDate.parse(day);
        
        User user = userRepository
                    .findByUserName(UserDetailsServiceImpl.getCurrentUserName())
                    .stream().findFirst().get(); 
                                             
        return getWorkEntries(user, dt);
    }
    
    private List<WorkEntry> getWorkEntries(User user, LocalDate dt) {
        List<TaskInWeek> tasksInWeek = weekEntryService.getWorkWeek(user, dt);
        LOG.trace("Tasks in week " + dt + ": " + tasksInWeek.size());
                
        List<WorkEntry> wes = tasksInWeek
                .stream()
                .flatMap(tiw ->  tiw.getDailyEntries())
                                    .filter(we -> we.getAtDate().isEqual(dt)   // only of correct day
                        )
                .collect(Collectors.toList());
                
        LOG.debug("workEntries at day " + dt + ": " + wes.size());
        wes.forEach(we -> LOG.debug("retuned workEntry: " + we));
        
        return wes;    
    }
}
