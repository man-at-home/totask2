package org.regele.totask2.controller;

import java.util.List;

import javax.ws.rs.Produces;

import org.regele.totask2.model.Project;
import org.regele.totask2.model.ProjectRepository;
import org.regele.totask2.service.ProjectPlanDataService;
import org.regele.totask2.service.UserCachingService;
import org.regele.totask2.util.ProjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;


/** 
 * project gantt chart rendering (using ganttView).
 * 
 * @see org.regele.totask2.model.Project
 * @see org.regele.totask2.model.TaskAssignment
 * @see <a href="https://github.com/thegrubbsian/jquery.ganttView">ganttView javascript control</a>
 * 
 * @author man-at-home
 * @since  2014-11-15
 */
@Controller
@Api(value="project plan REST-API", description = "totask2 projects gantt view chart data REST API")
public class ProjectPlanController {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProjectPlanController.class);
 

    @Autowired private ProjectRepository    projectRepository;    
    @Autowired private UserCachingService   userCachingService;
    @Autowired private ProjectPlanDataService projectPlanDataService;

    /** 
     * render view template with gantt chart. 
     * 
     * @param id  project.id /plan/project/{id}
     * @exception ProjectNotFoundException
     * */
    @RequestMapping(value = "/plan/project/{id}", method = RequestMethod.GET)
    public String showProjectPlan(@PathVariable final long id, final Model model) {
        
        LOG.debug("show plan for project " + id);
        
        Project project = projectRepository.findOne(id);
        if (project == null) {
            throw new ProjectNotFoundException("project " + id + " not found to chart.");            
        }
        
        Project.dump(project);
        model.addAttribute("project", project);  
        
        LOG.debug("will chart for " + project);
        return "projectPlan";
    }

    
    /** 
     * REST: provide {@link org.regele.totask2.model.Task} data in json form ({@link ProjectPlanData} for rendering gantt chart. 
     * 
     * @param id  project.id /plan/data/project/{id}
     * @exception ProjectNotFoundException
     * 
     */
    @RequestMapping(value = "/REST/plan/project/{id}", method = RequestMethod.GET)
    @Produces("application/json")
    @ResponseBody 
    List<ProjectPlanData> plan(@PathVariable final long id) 
    {        
        LOG.debug("providing json plan2 data for project " + id);
        
        Project project = projectRepository.findOne(id); 
        List<ProjectPlanData> planData = projectPlanDataService.createProjectPlanData(project);
            
        LOG.debug("json: " + planData);
        return planData;
    }    
    
}
