package org.regele.totask2.controller;

import org.regele.totask2.model.Project;
import org.regele.totask2.model.ProjectRepository;
import org.regele.totask2.model.WorkEntryRepository;
import org.regele.totask2.util.ProjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import javax.validation.Valid;


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
    @Autowired
    private WorkEntryRepository workEntryRepository;
    
  
    /** list all projects. */
    @RequestMapping("/weekEntry")
    public String weekEntry(Model model) {        
        LOG.debug("weekEntry");
        
   //     List<Project> projects = workEntryRepository.findAll();
        model.addAttribute("tasksInWeek", null);        
        
        LOG.debug("serving entries.");
        return "weekEntry";
    }    
        
}//class
