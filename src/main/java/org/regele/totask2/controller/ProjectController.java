package org.regele.totask2.controller;

import org.regele.totask2.model.Project;
import org.regele.totask2.model.ProjectRepository;
import org.regele.totask2.model.User;
import org.regele.totask2.service.ReportGenerator;
import org.regele.totask2.service.ReportGenerator.ReportOutputFormat;
import org.regele.totask2.service.UserCachingService;
import org.regele.totask2.util.ApplicationAssert;
import org.regele.totask2.util.Authorisation;
import org.regele.totask2.util.ProjectNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.ws.rs.Produces;


/** admin ui projects.
 * - add {@link Project}
 * - change {@link Project}
 * 
 * @author man-at-home
 * @since  2014-08-14
 */
@Controller
@Api(value="project REST-API", description = "totask2 projects REST API")
public class ProjectController {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectController.class);

    @Autowired private ProjectRepository    projectRepository;    
    @Autowired private ReportGenerator      reportGenerator;        
    @Autowired private CounterService       counterService;
    @Autowired private UserCachingService   userCachingService;
 
    
    /** REST API: /REST/Projects. */
    @RequestMapping(value = "/REST/projects", method = RequestMethod.GET)
    @Produces("application/json")
    @ApiOperation(value = "REST/projects", notes = "return all known projects")
    @ApiResponses(value = { @ApiResponse( code = 200, message = "ok") })
    @ResponseBody public List<Project> restProjects()
    {
        return projectRepository.findAll();        
    }
    
    /** common choice list in view templates: users. */
    @ModelAttribute("projectLeads")
    public List<User> getProjectLeads()
    {
        return userCachingService.getCachedUsers();
    }
   
    
    /** access rights. */
    @ModelAttribute("isNewAllowed")
    public boolean isNewAllowed()
    {
        return Authorisation.isAdmin( SecurityContextHolder.getContext().getAuthentication());
    }

    /** currently logged in @see user */
    @ModelAttribute("user")
    public User getUser()
    {
        return userCachingService.getCachedUser(SecurityContextHolder.getContext().getAuthentication());
    }    
    
    /** 
     * convert projectLeads user.id <-> user instance in template with 
     * <code>th:field="*{{projectLeads}}"</code> in view <i>templates/editProject.html</i>.
     * 
     * @see <a href="https://github.com/brianreavis/selectize.js">selectize.js</a>
     * @see User
     * @see Project#getProjectLeads()
     * @see UserCollectionEditor
     * */
    @InitBinder
    public void initProjectLeadBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(
                Set.class, 
                "projectLeads", 
                new UserCollectionEditor(Set.class, userCachingService));
    }
       
  
    /** list all {@link Project}s. */
    @RequestMapping("/projects")
    public String projects(final Model model) {       
        LOG.debug("projects");       
        
        List<Project> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);        
        
        LOG.debug("serving " + projects.size() + " projects.");
        return "projects";
    }    


    

    /** show edit page for an existing project. GET. 
     * 
     * @param id project.id /project/{id}
     * @exception ProjectNotFoundException
     * */
    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public String showProject(@PathVariable final long id, final Model model) {
        
        LOG.debug("show project " + id);
        
        Project project = projectRepository.findOne(id);
        if (project == null) {
            throw new ProjectNotFoundException("project " + id + " not found to show.");            
        }
        
        ApplicationAssert.assertNotNull("projectLeads empty, project " + project.getId() , project.getProjectLeads().size());        
        Project.dump(project);
        model.addAttribute("project", project);  
        model.addAttribute("isEditAllowed", project.isEditAllowed(getUser()) );
        
        LOG.debug("will show " + project);
        return "editProject";
    }    
    
    /** show edit page for a new project. GET. */
    @Secured(Authorisation.ROLE_ADMIN)
    @RequestMapping(value = "/project/new", method = RequestMethod.GET)
    public String newProject(final Model model) {       
        LOG.debug("new project");
        
        Project project = new Project();
        model.addAttribute("project", project);        
        model.addAttribute("isEditAllowed", isNewAllowed() );
        
        LOG.debug("will edit new " + project);
        return "editProject";
    }
    
    /** save edited project. POST. */
    @Secured(Authorisation.ROLE_ADMIN)
    @RequestMapping(value = "/project/save", method = RequestMethod.POST)
    public String saveProject(@Valid final Project project, final BindingResult bindingResult, final ModelMap model) {        
        LOG.debug("saveProject(" + project +  ")");
        
        User.dumpAuthentication();
        
        if (bindingResult.hasErrors()) {
            return "editProject";
        }
                
        Project.dump(project);

        Authorisation.require(isNewAllowed());
        this.projectRepository.saveAndFlush(project);
        model.clear();       
        
        if( this.counterService != null ) 
            counterService.increment("totask2.projects.changed");        
        
        LOG.debug("saved " + project + ", now redirecting.");
        return "redirect:/projects";
    }    
    



    /**  
     * generate an pdf or excel report from project data. 
     * 
     * @param reportFormat excel or pdf /projects/report/{reportFormat}
     * @see ReportGenerator
     * */
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
