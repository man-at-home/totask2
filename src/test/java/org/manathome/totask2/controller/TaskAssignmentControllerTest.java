package org.manathome.totask2.controller;

import static org.hamcrest.Matchers.containsString;
import static org.manathome.totask2.controller.SecurityRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Test;
import org.manathome.totask2.model.TaskAssignment;
import org.manathome.totask2.model.TaskAssignmentRepository;
import org.manathome.totask2.model.TaskRepository;
import org.manathome.totask2.model.UserRepository;
import org.manathome.totask2.util.AAssert;
import org.manathome.totask2.util.TestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

/** 
 * testing task assignments controller.
 * 
 * @author man-at-home
 * @since  2014-12-14
 */
public class TaskAssignmentControllerTest extends ControllerTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(TaskAssignmentControllerTest.class);

    @Autowired private TaskAssignmentRepository taskAssignmentRepository;  
    @Autowired private UserRepository userRepository;
    @Autowired private TaskRepository taskRepository;
    
    
    /** testing task/1/assignments. (lists current assignments). */
    @Test
    public void testAssignmentsForTask() throws Exception {
        
        LOG.debug("request /task/1/assignments");
        
        MvcResult result =
        this.mockMvc.perform(get("/task/1/assignments")
                .with(user("unit-test-admin").roles("ADMIN", "USER"))                               
                )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("task assignments to user")))
        .andExpect(content().string(containsString("assign new user to task..")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    } 
    
    /** testing task/1/assignment/new. (add new assignment to task). */
    @Test
    public void testNewTaskAssignment() throws Exception {
        
        LOG.debug("request /task/1/assignment/new");
        
        MvcResult result =
        this.mockMvc.perform(get("/task/1/assignment/new")
                .with(user("unit-test-admin").roles("ADMIN", "USER"))                               
                )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("userIdRef")))
        .andExpect(content().string(containsString(".autocomplete")))
        .andReturn();
        
        LOG.debug("response:" + result.getResponse().getContentAsString().replaceAll("\\r|\\n", ""));
    }  

    /** testing /assignment/1. */
    @Test
    public void testShowAssignmentToEdit() throws Exception {
        
        LOG.debug("request /assignment/1");
        
        this.mockMvc.perform(get("/assignment/1")
                .with(user("unit-test-admin").roles("ADMIN", "USER"))                               
                )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("assignment")))
        .andReturn();
    }  
    
    /** test assignment/delete (delete). */
    @Test
    @Transactional
    public void testDeleteTaskAssignment() throws Exception {
    
        LOG.debug("/assignment/delete (delete");
        
        TaskAssignment ta = AAssert.checkNotNull(
                taskAssignmentRepository.saveAndFlush(
                                AAssert.checkNotNull(
                                        taskRepository.getOne(1L))
                                        .addAssignment(userRepository.getOne(TestConstants.ADMIN_USER))))
                        ;

        Assert.assertNotNull("assignment to delete", taskAssignmentRepository.findOne(ta.getId())); 
        
        LOG.debug("/assignment/delete id:" + AAssert.checkPositive(ta.getId()));

        this.mockMvc.perform(post("/assignment/delete")
                .with(user("unit-test-admin").roles("ADMIN", "USER"))                               
                .param("id", ta.getId() + "")
                )
        .andExpect(status().is3xxRedirection());
                
        Assert.assertNull("no assignment anymore", taskAssignmentRepository.findOne(ta.getId())); 
    }

}
