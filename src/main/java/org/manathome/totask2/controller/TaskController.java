package org.manathome.totask2.controller;

import org.manathome.totask2.model.Project;
import org.manathome.totask2.model.ProjectRepository;
import org.manathome.totask2.model.Task;
import org.manathome.totask2.model.TaskRepository;
import org.manathome.totask2.model.User;
import org.manathome.totask2.service.UserCachingService;
import org.manathome.totask2.util.Authorisation;
import org.manathome.totask2.util.ProjectNotFoundException;
import org.manathome.totask2.util.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

/** 
 * admin ui {@link Task}s.
 * 
 * <ul>
 *  <li>add task to project</li>
 *  <li>remove task from project</li>
 *  <li>change task details</li>
 * </ul> 
 * 
 * @see TaskAssignmentController
 * @see Task
 * 
 * @author man-at-home
 * @since  2014-08-21
 */
@Transactional
@Controller
public class TaskController {
    
    private static final Logger LOG = LoggerFactory.getLogger(TaskController.class);

    /** task repository. */
    @Autowired private TaskRepository taskRepository;

    /** project repository. */
    @Autowired private ProjectRepository projectRepository;   
    
    @Autowired private UserCachingService   userCachingService;
    
    
    
    /** currently logged in @see user. */
    @ModelAttribute("user")
    public User getUser() {
        return userCachingService.getCachedUser(SecurityContextHolder.getContext().getAuthentication());
    }  

// tag::developer-manual-controller[]
    /** show all tasks for given project. 
     * 
     * @param id project.id to show tasks for.
     * */
    @RequestMapping(value = "/project/{id}/tasks", method = RequestMethod.GET)          // <1>
    public String tasksForProject(@PathVariable final long id, final Model model) {
        
        LOG.debug("tasks for project " + id);
   
        Project project  = projectRepository.findOne(id);
        List<Task> tasks = taskRepository.findByProjectId(id);                          // <2>
        
        model.addAttribute("tasks", tasks);                                             // <3>
        model.addAttribute("projectId", id);
        model.addAttribute("isEditAllowed", project.isEditAllowed(getUser()));


        LOG.debug("serving " + tasks.size() + " tasks for project " + id);
        return "tasks";
    }
// end::developer-manual-controller[] 
    
    /** show edit page /task/{id} for an existing task. GET. 
     * 
     * @param id task.id to show/edit
     * */
    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public String editTask(@PathVariable final long id, final Model model) {
        
        LOG.debug("edit task " + id);
        
        Task task = taskRepository.findOne(id);
        boolean isEditAllowed = task.isEditAllowed(getUser());
        model.addAttribute("task", task);          
        model.addAttribute("isEditAllowed", isEditAllowed);
        
        LOG.debug("will edit " + task.getName());
        return "editTask";
    }
    
    /** 
     * delete an existing task. POST.
     *  
     * @param id  task.id to delete.
     * @exception TaskNotFoundException
     */
    @RequestMapping(value = "/task/delete", method = RequestMethod.POST)
    public String deleteTask(@RequestParam long id, final Model model) {
        
        LOG.debug("delete task " + id);
        
        Task taskToDelete = taskRepository.findOne(id);
        if (taskToDelete == null) {
            throw new TaskNotFoundException("task " + id + " not found for deletion.");
        }
        Authorisation.require(taskToDelete.isEditAllowed(getUser()));   
        
        long projectId = taskToDelete.getProject().getId();
        
        taskRepository.delete(taskToDelete);
        taskRepository.flush();
        
        LOG.debug("deleted task " + id);
        return "redirect:/project/" + projectId + "/tasks";
    }
    
    /** 
     * show edit page for a new task. GET. 
     * 
     * @param projectId part of GET path, id of project for the new task.
     * @exception ProjectNotFoundException
     * */
    @RequestMapping(value = "project/{projectId}/task/new", method = RequestMethod.GET)
    public String newTask(@PathVariable final long projectId, final Model model) {       
        LOG.debug("new task for project " + projectId);
        
        Project project = this.projectRepository.findOne(projectId);
        if (project == null) {
            throw new ProjectNotFoundException("project " + projectId + " not found creating new task.");
        }
        Authorisation.require(project.isEditAllowed(getUser()));   
        Task task = project.createTask();
        model.addAttribute("task", task);        
        model.addAttribute("isEditAllowed", task.isEditAllowed(getUser()));
        
        LOG.debug("will edit new task " + task);
        return "editTask";
    }    
    
    
    /** save edited task. POST. 
     * 
     * @param task html values (edited), updating to db.
     * */
    @RequestMapping(value = "/task/save", method = RequestMethod.POST)
    public String editTaskSave(@Valid final Task task, final BindingResult bindingResult, final ModelMap model) {        
        LOG.debug("editTaskSave(" + task + ")");
        
        User.dumpAuthentication();
        if (bindingResult.hasErrors()) {
            model.addAttribute("task", task);        
            model.addAttribute("isEditAllowed", task.isEditAllowed(getUser()));
            return "editTask";
        }
                
        Authorisation.require(task.isEditAllowed(getUser()));               
        Task savedTask  = this.taskRepository.saveAndFlush(task);
        model.clear();        
        
        LOG.debug("saved " + savedTask + ", now redirecting.");
        return "redirect:/project/" + savedTask.getProject().getId() + "/tasks";
    }    
    
}
