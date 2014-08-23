package org.regele.totask2.controller;

import org.regele.totask2.model.Project;
import org.regele.totask2.model.ProjectRepository;
import org.regele.totask2.model.Task;
import org.regele.totask2.model.TaskRepository;
import org.regele.totask2.util.ProjectNotFoundException;
import org.regele.totask2.util.TaskNotFoundException;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import javax.validation.Valid;



/** 
 * admin ui tasks.
 * 
 * - add task to project
 * - remove task from project
 * - change task details
 * 
 * @author Manfred
 * @since  2014-08-21
 */
@Controller
public class TaskController {
    
    private static final Logger LOG = LoggerFactory.getLogger(TaskController.class);

    /** task repository. */
    @Autowired
    private TaskRepository taskRepository;

    /** project repository. */
    @Autowired
    private ProjectRepository projectRepository;    

    /** simple hello world. */
    @RequestMapping(value = "/project/{id}/tasks", method = RequestMethod.GET)
    public String tasksForProject(@PathVariable final long id, final Model model) {
        
        LOG.debug("tasks for project " + id);
   
        List<Task> tasks = taskRepository.findByProjectId(id);
        
        model.addAttribute("tasks", tasks);           
        model.addAttribute("projectId", id);
        
        LOG.debug("serving " + tasks.size() + " tasks for project " + id);
        return "tasks";
    }
    
    /** show edit page for an existing task. GET. */
    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public String editTask(@PathVariable final long id, final Model model) {
        
        LOG.debug("edit task " + id);
        
        Task task = taskRepository.getOne(id);
        model.addAttribute("task", task);        
        
        LOG.debug("will edit " + task);
        return "editTask";
    }
    
    /** delete an existing task. POST. */
    @RequestMapping(value = "task/delete", method = RequestMethod.POST)
    public String deleteTask(@RequestParam long id, final Model model) {
        
        LOG.debug("delete task " + id);
        
        Task taskToDelete = taskRepository.findOne(id);
        if (taskToDelete == null) {
            throw new TaskNotFoundException("task " + id + " not found for deletion.");
        }
        
        long projectId = taskToDelete.getProject().getId();
        
        taskRepository.delete(taskToDelete);
        taskRepository.flush();
        
        LOG.debug("deleted task " + id);
        return "redirect:/project/" + projectId + "/tasks";
    }
    
    /** show edit page for a new task. GET. */
    @RequestMapping(value = "project/{projectId}/task/new", method = RequestMethod.GET)
    public String newTask(@PathVariable final long projectId, final Model model) {       
        LOG.debug("new task for project " + projectId);
        
        Project project = this.projectRepository.findOne(projectId);
        if (project == null) {
            throw new ProjectNotFoundException("project " + projectId + " not found creating new task.");
        }
        Task task = project.createTask();
        model.addAttribute("task", task);        
        
        LOG.debug("will edit new task " + task);
        return "editTask";
    }    
    
    
    /** save edited task. POST. */
    @RequestMapping(value = "/task/save", method = RequestMethod.POST)
    public String editTaskSave(@Valid final Task task, final BindingResult bindingResult, final ModelMap model) {        
        LOG.debug("editTaskSave(" + task + ")");
        
        if (bindingResult.hasErrors()) {
            return "editTask";
        }
        
        Task savedTask  = this.taskRepository.saveAndFlush(task);
        model.clear();        
        
        LOG.debug("saved " + savedTask + ", now redirecting.");
        return "redirect:/project/" + savedTask.getProject().getId() + "/tasks";
    }    
    
}
