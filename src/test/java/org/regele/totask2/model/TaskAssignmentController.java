package org.regele.totask2.model;

import java.util.List;

import org.regele.totask2.util.TaskAssignmentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/** 
 * manage task assignments to users. 
 * 
 * @author Manfred
 * @since  2014-10-27
 * */
@Controller
public class TaskAssignmentController {

    private static final Logger LOG = LoggerFactory.getLogger(TaskAssignmentController.class);

    /** task repository. */
    @Autowired private TaskRepository taskRepository;

    /** task a repository. */
    @Autowired private TaskAssignmentRepository taskAssignmentRepository;
    
    /** show all tasks for given project. */
    @RequestMapping(value = "/task/{id}/assignments", method = RequestMethod.GET)
    public String tasksForProject(@PathVariable final long id, final Model model) {
        
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
    public String deleteTask(@RequestParam long id, final Model model) {
        
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
    public String editTask(@PathVariable final long id, final Model model) {
        
        LOG.debug("edit task assignment " + id);
        
        TaskAssignment assignment = taskAssignmentRepository.getOne(id);
        model.addAttribute("task", assignment.getTask());
        model.addAttribute("assignment", assignment);        
        
        LOG.debug("will edit " + assignment);
        return "editTaskAssignment";
    }
    
}
