package org.regele.totask2.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.regele.totask2.model.Project;
import org.regele.totask2.model.ProjectRepository;
import org.regele.totask2.model.Task;
import org.regele.totask2.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


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
    @RequestMapping(value = "/tasks", params = {"tasksByProject"}, method = RequestMethod.GET)
    public String tasks(final Model model, final HttpServletRequest req) {
        
        LOG.debug("tasks");
   
        long projectId = Long.parseLong( req.getParameter("tasksByProject") );
       
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        
        model.addAttribute("tasks", tasks);           
        model.addAttribute("projectId", projectId);
        
        LOG.debug("serving " + tasks.size() + " tasks for project " + projectId);
        return "tasks";
    }
    
    /** show edit page for an existing task. GET. */
    @RequestMapping(value = "/editTask", params = {"editTask"}, method = RequestMethod.GET)
    public String editTask(final Model model, final HttpServletRequest req) {
        
        LOG.debug("edit task");
        
        long taskId = Long.parseLong( req.getParameter("editTask") );
        Task task = taskRepository.getOne(taskId);
        model.addAttribute("task", task);        
        
        LOG.debug("will edit " + task);
        return "editTask";
    }
    
    /** show edit page for an existing task. GET. */
    @RequestMapping(value = "/editTask", params = {"deleteTask"}, method = RequestMethod.GET)
    public String deleteTask(final Model model, final HttpServletRequest req) {
        
        LOG.debug("delete task");
        
        long taskId = Long.parseLong( req.getParameter("deleteTask") );
        Task taskToDelete = taskRepository.getOne(taskId);
        long projectId = taskToDelete.getProject().getId();
        
        taskRepository.delete(taskToDelete);
        taskRepository.flush();
        
        LOG.debug("deleted task " + taskId);
        return "redirect:/tasks?tasksByProject=" + projectId;
    }
    
    /** show edit page for a new project. GET. */
    @RequestMapping(value = "/newTask", params = {"projectId"}, method = RequestMethod.GET)
    public String newTask(final Model model, final HttpServletRequest req) {       
        LOG.debug("new task");
        
        long projectId = Long.parseLong( req.getParameter("projectId") );
        Project project = this.projectRepository.getOne(projectId);
        
        Task task = project.createTask();
        model.addAttribute("task", task);        
        
        LOG.debug("will edit new task " + task);
        return "editTask";
    }    
    
    
    /** save edited task. POST. */
    @RequestMapping(value = "/editTask", method = RequestMethod.POST)
    public String editTaskSave(@Valid final Task task, final BindingResult bindingResult, final ModelMap model, final HttpServletRequest req) {        
        LOG.debug("editTaskSave(" + task + ")");
        
        if (bindingResult.hasErrors()) {
            return "editTask";
        }
        
        Task savedTask  = this.taskRepository.saveAndFlush(task);
        model.clear();        
        
        LOG.debug("saved " + savedTask + ", now redirecting.");
        return "redirect:/tasks?tasksByProject=" + savedTask.getProject().getId();
    }    
    
}
