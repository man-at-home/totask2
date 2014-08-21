package org.regele.totask2.model;

import static org.junit.Assert.*;


import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.regele.totask2.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;


/** testing db access. */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
public class TaskRepositoryTest {
    

    private static final Logger LOG = LoggerFactory.getLogger(TaskRepositoryTest.class);
    
    /** task repository under test. */
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LOG.debug("testing db task access ");
    }

    /** userRepository.count. */
    @Test   
    public void testReadTasks() {
        assertNotNull("task repo not injected", taskRepository);
        assertTrue("task table access not possible" ,  taskRepository.count() >= 0);
    }

    /** save tasks belonging to project. */
    @Test   
    public void testStoreTasksInNewProject() {
        Project project = new Project();
        project.setName("junit tests tasks-project");
        
        project = projectRepository.saveAndFlush(project);
        LOG.debug("saved  project " + project + " id:" + project.getId());

        Task t1 = project.createTask();
        t1.setName("junit task 1");
        Task t2 = project.createTask();
        t2.setName("junit task 2");
        
        assertEquals("project " + project + " has both tasks", 2, project.getTasks().count());
        
        long idt1 = taskRepository.saveAndFlush(t1).getId();
        long idt2 = taskRepository.saveAndFlush(t2).getId();
        
        assertTrue("t1 with id", idt1 > 0);
        assertTrue("t2 with id", idt2 > 0);
        
        LOG.debug("reload task " + idt2);
        Task refechtedT2 = taskRepository.getOne(idt2);
        assertEquals("task with project saved (id)", project.getId(), refechtedT2.getProject().getId());
        assertEquals("task with project (name)", project.getName(), refechtedT2.getProject().getName());
        
        LOG.debug("reload project " + project.getId());
        Project refechtedProject = projectRepository.getOne(project.getId());
        assertTrue("all tasks in project " + refechtedProject.getTasks().count() , refechtedProject.getTasks().count() >= 2);
    }
    
    /** read existing tasks (data.sql). */
    @Test   
    public void testReadTasksInProject() {
        Project totask2Project = projectRepository.findByName("totask2").get(0);
        assertEquals("all tasks in project ", 2,  totask2Project.getTasks().count());
        totask2Project.getTasks().allMatch( t -> t.getProject().getName().equals("develop") || t.getProject().getName().equals("support"));
    
        assertEquals("support task not found" , 1, taskRepository.findByName("support").size());
    }
    
}

