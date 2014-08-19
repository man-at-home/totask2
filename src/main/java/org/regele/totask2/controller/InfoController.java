package org.regele.totask2.controller;

import org.regele.totask2.model.ProjectRepository;
import org.regele.totask2.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** admin/info web controller. */
@Controller
public class InfoController {
    
    private static final Logger LOG = LoggerFactory.getLogger(InfoController.class);
    
    /** project repository. */
    @Autowired
    private ProjectRepository projectRepository;
 
    /** task repository. */
    @Autowired
    private TaskRepository taskRepository;

    /** simple hello world. */
    @RequestMapping("/greeting")
    public String greeting(
            @RequestParam(value = "name", required = false, defaultValue = "World") String name,
            Model model) {
        
        LOG.debug("info(" + name + ")");
        model.addAttribute("name", name);
        return "greeting";
    }
    
    
    /** showing actual db contents. */
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
