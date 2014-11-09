package org.regele.totask2.controller;


import org.regele.totask2.model.Task;
import org.regele.totask2.model.TaskAssignment;
import org.regele.totask2.model.TaskAssignmentRepository;
import org.regele.totask2.model.TaskRepository;
import org.regele.totask2.model.User;
import org.regele.totask2.model.UserRepository;
import org.regele.totask2.service.UserDetailsServiceImpl;
import org.regele.totask2.util.TaskAssignmentNotFoundException;
import org.regele.totask2.util.TaskNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
    @Autowired private UserRepository userRepository;
   
    
    /** show all tasks for given project. */
    @RequestMapping(value = "/task/{id}/assignments", method = RequestMethod.GET)
    public String assignmentsForTask(@PathVariable final long id, final Model model) {
        
        LOG.debug("task assignments for task " + id);
  
        Task task = taskRepository.findOne(id);
        List<TaskAssignment> assignments = taskAssignmentRepository.findByTask(task);
        
        model.addAttribute("task", task);           
        model.addAttribute("assignments", assignments);
        
        LOG.debug("serving " + assignments.size() + " assignments for task " + task);
        return "taskAssignments";
    }   
    
    /** delete an existing assignment. POST. */
    @RequestMapping(value = "assignment/delete", method = RequestMethod.POST)
    public String deleteAssignment(@RequestParam final long id, final Model model) {
        
        LOG.debug("delete taskAssignment " + id);
        
        TaskAssignment assignmentToDelete = taskAssignmentRepository.findOne(id);
        if (assignmentToDelete == null) {
            throw new TaskAssignmentNotFoundException("assignment " + id + " not found for deletion.");
        }
        
        long taskId = assignmentToDelete.getTask().getId();
        
        taskAssignmentRepository.delete(assignmentToDelete);
        taskAssignmentRepository.flush();
        
        LOG.debug("deleted task assignment " + id);
        return "redirect:/task/" + taskId + "/assignments";
    }
    
    /** show edit page for an existing taskAssignment. GET. */
    @RequestMapping(value = "/assignment/{id}", method = RequestMethod.GET)
    public String editAssignment(@PathVariable final long id, final Model model) {
        
        LOG.debug("edit task assignment " + id);
        
        TaskAssignment assignment = taskAssignmentRepository.getOne(id);
        model.addAttribute("task", assignment.getTask());
        model.addAttribute("assignment", assignment);        
        
        LOG.debug("will edit " + assignment);
        return "editTaskAssignment";
    }
    
    
    /** show edit page for a new assignment. GET. */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "task/{taskId}/assignment/new", method = RequestMethod.GET)
    public String newAssignment(@PathVariable final long taskId, final Model model) {       
        LOG.debug("new assignment for task " + taskId);

        User user = userRepository.findByUserName(UserDetailsServiceImpl.getCurrentUserName()).stream().findFirst().get();               
        Task task = this.taskRepository.findOne(taskId);
        
        if (task == null) {
            throw new TaskNotFoundException("task " + taskId + " not found creating new assignment.");
        }
        TaskAssignment assignment = task.addAssignment(user);
        
        model.addAttribute("task", task);
        model.addAttribute("assignment", assignment);        
        
        LOG.debug("will edit new assignment " + assignment + " givent task " + task);
        return "editTaskAssignment";
    }  
    
    
    /** save edited assignment. POST. */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/assignment/save", method = RequestMethod.POST)
    public String editAssignmentSave(@Valid @ModelAttribute final TaskAssignment assignment, final BindingResult bindingResult, final ModelMap model) {        
        LOG.debug("editTaskAssignmentSave(" + assignment + ")");
                
        if (bindingResult.hasErrors()) {
            LOG.debug("editTaskAssignmentSave validation errors " + bindingResult);
            model.addAttribute("task", assignment.getTask());
            model.addAttribute("assignment", assignment);        
            return "editTaskAssignment";
        }
        
        TaskAssignment savedTaskAssignment  = this.taskAssignmentRepository.saveAndFlush(assignment);
        model.clear();        
        
        LOG.debug("saved " + savedTaskAssignment + ", now redirecting.");
        return "redirect:/task/" + savedTaskAssignment.getTask().getId() + "/assignments";
    }
    
}
