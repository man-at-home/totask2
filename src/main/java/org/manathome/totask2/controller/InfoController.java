package org.manathome.totask2.controller;

import org.manathome.totask2.model.ProjectRepository;
import org.manathome.totask2.model.TaskRepository;
import org.manathome.totask2.util.Authorisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/** admin/info web controller. */
@Controller
public class InfoController {
    
    private static final Logger LOG = LoggerFactory.getLogger(InfoController.class);
    
    @Autowired private ProjectRepository projectRepository;
    @Autowired private TaskRepository taskRepository;

    
    /** showing actual db contents. */
    @Secured(Authorisation.ROLE_ADMIN)
    @RequestMapping("/dbinfo")
    public String dbinfo(final Model model) {
       
        LOG.debug("dbinfo");
        
        long projectCount = projectRepository.count();
        long taskCount    = taskRepository.count();
                
        
        model.addAttribute("projectCount", projectCount);
        model.addAttribute("taskCount", taskCount);
        return "dbinfo";
    }    

}// class
