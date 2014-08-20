package org.regele.totask2.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.regele.totask2.model.Project;
import org.regele.totask2.model.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/** admin ui projects.
 * - add project
 * - change project
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
    
  
    /** simple hello world. */
    @RequestMapping("/projects")
    public String projects(Model model) {
        
        LOG.debug("projects");
        
        List<Project> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);        
        
        LOG.debug("serving " + projects.size() + " projects.");
        return "projects";
    }

    
    /** show edit page for an existing project. GET. */
    @RequestMapping(value = "/editProject", params = {"editProject"}, method = RequestMethod.GET)
    public String editProject(final Model model, final HttpServletRequest req) {
        
        LOG.debug("edit project");
        
        long projectId = Long.parseLong( req.getParameter("editProject") );
        Project project = projectRepository.getOne( projectId );
        model.addAttribute("project", project);        
        
        LOG.debug("will edit " + project);
        return "editProject";
    }
    
    /** show edit page for a new project. GET. */
    @RequestMapping(value = "/newProject", method = RequestMethod.GET)
    public String newProject(final Model model) {       
        LOG.debug("new project");
        
        Project project = new Project();
        model.addAttribute("project", project);        
        
        LOG.debug("will edit new " + project);
        return "editProject";
    }
    /** save edited project. POST. */
    @RequestMapping(value = "/editProject", method = RequestMethod.POST)
    public String editProjectSave(@Valid final Project project, final BindingResult bindingResult, final ModelMap model) {        
        LOG.debug("editProjectSave(" + project +  ")");
        
        if (bindingResult.hasErrors()) {
            return "editProject";
        }
                
        this.projectRepository.saveAndFlush(project);
        model.clear();        
        
        LOG.debug("saved " + project + ", now redirecting.");
        return "redirect:/projects";
    }    
    
}//class
