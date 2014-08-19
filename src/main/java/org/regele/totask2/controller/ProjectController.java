package org.regele.totask2.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.regele.totask2.model.Project;
import org.regele.totask2.model.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

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

    
    /** show edit page for a project. */
    @RequestMapping(value = "/projects", params = {"editProject"})
    public String editProject(final Model model, final HttpServletRequest req) {
        
        LOG.debug("edit project");
        
        long projectId = Long.parseLong( req.getParameter("editProject") );
        Project project = projectRepository.getOne( projectId );
        model.addAttribute("project", project);        
        
        LOG.debug("will edit " + project);
        return "editProject";
    }

    
}//class
