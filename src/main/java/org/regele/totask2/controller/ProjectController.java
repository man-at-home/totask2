package org.regele.totask2.controller;

import org.regele.totask2.model.Project;
import org.regele.totask2.model.ProjectRepository;
import org.regele.totask2.service.ReportGenerator;
import org.regele.totask2.service.ReportGenerator.ReportOutputFormat;
import org.regele.totask2.util.ProjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.validation.Valid;


/** admin ui projects.
 * - add project
 * - change project
 * 
 * @see TaskController
 * 
 * @author Manfred
 * @since  2014-08-14
 */
@Controller
public class ProjectController {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectController.class);

    /** project repository. */
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private ReportGenerator reportGenerator;
        
    @Autowired 
    private CounterService counterService;
       
  
    /** list all projects. */
    @Secured("ROLE_ADMIN")
    @RequestMapping("/projects")
    public String projects(Model model) {
        
        LOG.debug("projects");
        
        dumpUser();
        
        List<Project> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);        
        
        LOG.debug("serving " + projects.size() + " projects.");
        return "projects";
    }    
    
    private void dumpUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if( auth != null) {
            LOG.debug("user: " + auth.getName() + " " + auth.isAuthenticated() + " roles " + auth.getAuthorities().size()); //get logged in username
 
            auth.getAuthorities()
            .stream().
            forEach( ga ->  
                LOG.debug("  user-role:" + ga)
            );
        }
        else {
            LOG.debug("no auth.user user");
        }           
    }

    /** show edit page for an existing project. GET. */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public String showProject(@PathVariable final long id, final Model model) {
        
        LOG.debug("show project " + id);
        
        Project project = projectRepository.findOne(id);
        if (project == null) {
            throw new ProjectNotFoundException("project " + id + " not found to show.");
        }
        model.addAttribute("project", project);        
        
        LOG.debug("will show " + project);
        return "editProject";
    }    
    
    /** show edit page for a new project. GET. */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/project/new", method = RequestMethod.GET)
    public String newProject(final Model model) {       
        LOG.debug("new project");
        
        Project project = new Project();
        model.addAttribute("project", project);        
        
        LOG.debug("will edit new " + project);
        return "editProject";
    }
    
    /** save edited project. POST. */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/project/save", method = RequestMethod.POST)
    public String saveProject(@Valid final Project project, final BindingResult bindingResult, final ModelMap model) {        
        LOG.debug("saveProject(" + project +  ")");
        
        dumpUser();
        
        if (bindingResult.hasErrors()) {
            return "editProject";
        }
                
        this.projectRepository.saveAndFlush(project);
        model.clear();       
        
        if( this.counterService != null ) 
            counterService.increment("totask2.projects.changed");        
        
        LOG.debug("saved " + project + ", now redirecting.");
        return "redirect:/projects";
    }    
    
   
    /**  generate an pdf or excel report from project data. */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/projects/report/{reportFormat}", method = RequestMethod.GET)
    public ModelAndView getProjectsReport(@PathVariable final String reportFormat) {
        
        LOG.debug("getProjectsReport()");
        
        List<Project> reportData = projectRepository.findAll();
                       
        return reportGenerator
                .createReportModelView(
                        "projectsReport.jrxml" , 
                        reportFormat == null || reportFormat.contains("pdf") ? ReportOutputFormat.pdf : ReportOutputFormat.excel,
                        reportData
                        );
    }
    
}//class
