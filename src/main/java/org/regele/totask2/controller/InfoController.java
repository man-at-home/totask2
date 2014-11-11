package org.regele.totask2.controller;

import org.regele.totask2.model.ProjectRepository;
import org.regele.totask2.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/** admin/info web controller. */
@Controller
@Api(value="info REST-API", description = "totask2 info REST API")
public class InfoController {
    
    private static final Logger LOG = LoggerFactory.getLogger(InfoController.class);
    
    /** project repository. */
    @Autowired
    private ProjectRepository projectRepository;
 
    /** task repository. */
    @Autowired
    private TaskRepository taskRepository;

    /** REST API. */
    @RequestMapping(value = "/REST/info", method = RequestMethod.GET)    
    @ApiOperation(value = "info", notes = "simple REST test call")
    @ResponseBody public String restInfo()
    {        
        LOG.debug("info()");
        return "greeting from totask2 to you, the caller of this info REST API.";
    }
    
    
    /** simple hello world. */
    @Secured("ROLE_USER")
    @ApiIgnore
    @RequestMapping("/greeting")
    public String greeting(
            @RequestParam(value = "name", required = false, defaultValue = "World") String name,
            Model model) {
        
        LOG.debug("info(" + name + ")");
        model.addAttribute("name", name);
        return "greeting";
    }
    
    
    /** showing actual db contents. */
    @Secured("ROLE_ADMIN")
    @RequestMapping("/dbinfo")
    @ApiIgnore
    public String dbinfo(final Model model) {
       
        LOG.debug("dbinfo");
        
        long projectCount = projectRepository.count();
        long taskCount    = taskRepository.count();
                
        
        model.addAttribute("projectCount", projectCount);
        model.addAttribute("taskCount", taskCount);
        return "dbinfo";
    }    

}// class
