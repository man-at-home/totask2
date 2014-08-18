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

import java.util.List;
import javax.transaction.Transactional;


/** testing db access. */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringApplicationConfiguration(classes = { Application.class })
public class ProjectRepositoryTest {
    

    private static final Logger LOG = LoggerFactory.getLogger(ProjectRepositoryTest.class);
    
    /** user repository under test. */
    @Autowired
    private ProjectRepository projectRepository;
    
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LOG.debug("testing db project access ");
    }

    /** userRepository.count. */
    @Test   
    public void testReadProjects() {
        assertNotNull("project repo not injected", projectRepository);
        assertTrue("project table access not possible" ,  projectRepository.count() >= 0);
    }

    /** save, getOne. */
    @Test   
    public void testStoreProject() {
        Project project = new Project();
        project.setName("junit tests project");
        
        Project savedProject = projectRepository.save(project);
        LOG.debug("saved new project " + savedProject + " id:" + savedProject.getId());
        
        assertTrue("id for project generated" , savedProject.getId() > 0);
        assertTrue("new project with id exists", projectRepository.exists(savedProject.getId()));
        
        Project refetchedProject = projectRepository.getOne(savedProject.getId());
        assertEquals("name stored", project.getName() , refetchedProject.getName());
    }
    
    /** save, delete, findOne. */
    @Test   
    public void testStoreAndDeleteProject() {
        Project project = new Project();
        project.setName("junit tests project testStoreAndDeleteUser");
        
        Project savedProject = projectRepository.save(project);
        LOG.debug("Saved new project " + savedProject + " id:" + savedProject.getId());
 
        long id = savedProject.getId();
        
        projectRepository.delete(savedProject);
        Project notToBeFoundProject = projectRepository.findOne(id);
        assertNull("deleted project found", notToBeFoundProject);
    }

    /** find totask2 project inserted by data.sql. */
    @Test   
    public void testReadAdminUser() {
        List<Project> totask2Projects = projectRepository.findByName("totask2");
        assertEquals("project not found", 1, totask2Projects.size());
        LOG.debug("found totask2 project: " + totask2Projects.get(0));
        assertEquals("totask2 name not correct", "totask2", totask2Projects.get(0).getName());
        assertEquals("totask2 not id 1", 1, totask2Projects.get(0).getId());
    }   
    
}

