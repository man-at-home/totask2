package org.manathome.totask2.controller;


import org.manathome.totask2.model.Task;
import org.manathome.totask2.model.TaskAssignment;
import org.manathome.totask2.model.TaskAssignmentRepository;
import org.manathome.totask2.model.TaskRepository;
import org.manathome.totask2.model.User;
import org.manathome.totask2.service.UserCachingService;
import org.manathome.totask2.util.Authorisation;
import org.manathome.totask2.util.TaskAssignmentNotFoundException;
import org.manathome.totask2.util.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import javax.validation.Valid;


/** 
 * manage task assignments to users. 
 * 
 * @author man-at-home
 * @since  2014-10-27
 * */
@Controller
public class TaskAssignmentController {

    private static final Logger LOG = LoggerFactory.getLogger(TaskAssignmentController.class);

    /** task repository. */
    @Autowired private TaskRepository taskRepository;

    /** task assignment repository. */
    @Autowired private TaskAssignmentRepository taskAssignmentRepository;
    
    /** user repository. */
    @Autowired private UserCachingService userCachingService;
    
    

    /** currently logged in @see user. */
    @ModelAttribute("user")
    public User getUser() {
        return userCachingService.getCachedUser(SecurityContextHolder.getContext().getAuthentication());
    }    
   
    
    /** show all tasks for given project. */
    @RequestMapping(value = "/task/{id}/assignments", method = RequestMethod.GET)
    public String assignmentsForTask(@PathVariable final long id, final Model model) {
        
        LOG.trace("task assignments for task " + id);
  
        Task task = taskRepository.findOne(id);
        Authorisation.require(task.isEditAllowed(getUser()));       

        List<TaskAssignment> assignments = taskAssignmentRepository.findByTask(task);
        
        model.addAttribute("task", task);           
        model.addAttribute("assignments", assignments);
        model.addAttribute("isEditAllowed", task.isEditAllowed(getUser()));
      
        
        LOG.debug("serving " + assignments.size() + " assignments for task " + task);
        return "taskAssignments";
    }   
    
    /** delete an existing assignment. POST. */
    @RequestMapping(value = "/assignment/delete", method = RequestMethod.POST)
    public String deleteAssignment(@RequestParam final long id, final Model model) {
        
        LOG.trace("delete taskAssignment " + id);
        
        TaskAssignment assignmentToDelete = taskAssignmentRepository.findOne(id);
        if (assignmentToDelete == null) {
            throw new TaskAssignmentNotFoundException("assignment " + id + " not found for deletion.");
        }     
        
        long taskId = assignmentToDelete.getTask().getId();
        Authorisation.require(assignmentToDelete.getTask().isEditAllowed(getUser()));       
        
        taskAssignmentRepository.delete(assignmentToDelete);
        taskAssignmentRepository.flush();
        
        LOG.debug("deleted task assignment " + id);
        return "redirect:/task/" + taskId + "/assignments";
    }
    
    /** show edit page for an existing taskAssignment. GET. */
    @Transactional
    @RequestMapping(value = "/assignment/{id}", method = RequestMethod.GET)
    public String editAssignment(@PathVariable final long id, final Model model) {
        
        LOG.trace("edit task assignment " + id);
        
        TaskAssignment assignment = taskAssignmentRepository.getOne(id);
        
        Authorisation.require(assignment.getTask().isEditAllowed(getUser()));       
        
        model.addAttribute("task", assignment.getTask());
        model.addAttribute("assignment", assignment);        
        
        LOG.debug("will edit " + assignment);
        return "editTaskAssignment";
    }
    
    
    /** show edit page for a new assignment. GET. 
     * 
     * @exception TaskNotFoundException
     * */
    @Transactional
    @RequestMapping(value = "task/{taskId}/assignment/new", method = RequestMethod.GET)
    public String newAssignment(@PathVariable final long taskId, final Model model) {       
        LOG.trace("new assignment for task " + taskId);

        User user = getUser();               
        Task task = this.taskRepository.findOne(taskId);
        
        if (task == null) {
            throw new TaskNotFoundException("task " + taskId + " not found creating new assignment.");
        }
        Authorisation.require(task.isEditAllowed(getUser()));    
        
        TaskAssignment assignment = task.addAssignment(user);
        
        model.addAttribute("task", task);
        model.addAttribute("assignment", assignment);        
        
        LOG.debug("will edit new assignment " + assignment + " givent task " + task);
        return "editTaskAssignment";
    }  
    
    
    /** save edited assignment. POST. */
    @Transactional
    @RequestMapping(value = "/assignment/save", method = RequestMethod.POST)
    public String editAssignmentSave(@Valid @ModelAttribute final TaskAssignment assignment, final BindingResult bindingResult, final ModelMap model) {        
        LOG.trace("editTaskAssignmentSave(" + assignment + ")");
                
        if (bindingResult.hasErrors()) {
            LOG.debug("editTaskAssignmentSave validation errors " + bindingResult);
            model.addAttribute("task", assignment.getTask());
            model.addAttribute("assignment", assignment);        
            return "editTaskAssignment";
        }

        Authorisation.require(assignment.getTask().isEditAllowed(getUser()));       
        
        TaskAssignment savedTaskAssignment  = this.taskAssignmentRepository.saveAndFlush(assignment);
        model.clear();        
        
        LOG.debug("saved " + savedTaskAssignment + ", now redirecting.");
        return "redirect:/task/" + savedTaskAssignment.getTask().getId() + "/assignments";
    }
    
}
