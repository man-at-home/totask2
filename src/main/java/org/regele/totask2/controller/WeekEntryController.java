package org.regele.totask2.controller;

import java.time.LocalDate;
import java.util.List;

import org.regele.totask2.model.TaskInWeek;
import org.regele.totask2.model.User;
import org.regele.totask2.model.UserRepository;
import org.regele.totask2.model.WorkEntryRepository;
import org.regele.totask2.service.WeekEntryService;
import org.regele.totask2.util.LocalDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/** main entry page for uses.
 * - display exactly one week of work
 * - all tasks, one entry per task an day.
 * 
 * @author Manfred
 * @since  2014-08-09
 */
@Controller
public class WeekEntryController {

    private static final Logger LOG = LoggerFactory.getLogger(WeekEntryController.class);

    /** work entry repository. */
    @Autowired private WorkEntryRepository workEntryRepository;
    
    @Autowired private UserRepository userRepository;
    
    private WeekEntryService getService() {
        return new WeekEntryService( this.workEntryRepository );
    }
    
    
  
    /** list all projects. */
    @RequestMapping("/weekEntry")
    public String weekEntry(Model model) {        
        LOG.debug("weekEntry");
                
        LocalDate dt   = LocalDate.now();        
        User      user = userRepository.getOne( 2L /* TestConstants.TestUser */ );   // TBF current User.
        
        List<TaskInWeek> tasksInWeek = getService().getWorkWeek(user, dt);
        
        model.addAttribute("localDate",     dt); 
        model.addAttribute("date" ,         LocalDateConverter.toDate(dt));
        model.addAttribute("tasksInWeek",   tasksInWeek);        
        
        LOG.debug("serving weekEntry for " + dt);
        return "weekEntry";
    }    
        
}//class
