package org.manathome.totask2.model;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.manathome.totask2.Application;
import org.manathome.totask2.util.TestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;


/** testing db access. */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringApplicationConfiguration(classes = { Application.class })
@WebAppConfiguration
public class ProjectRepositoryTest {
    

    private static final Logger LOG = LoggerFactory.getLogger(ProjectRepositoryTest.class);
    
    /** project repository under test. */
    @Autowired private ProjectRepository    projectRepository;
    @Autowired private UserRepository       userRepository;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LOG.debug("testing db project access ");
    }

    /** userRepository.count. */
    @Test   
    public void testReadProjects() {
        assertNotNull("project repo not injected", projectRepository);
        
        LOG.debug("read all projects..");
        projectRepository.findAll().stream().forEach(p -> LOG.debug(" findAll-projects: " + p.getName() + "/" + p.getId()));
        LOG.debug("read all projects done.");
        assertTrue("project table access not possible" ,  projectRepository.count() >= 1);
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
    public void testReadTotask2Project() {
        List<Project> totask2Projects = projectRepository.findByName("totask2");
        assertEquals("project not found", 1, totask2Projects.size());
        LOG.debug("found totask2 project: " + totask2Projects.get(0));
        assertEquals("totask2 name not correct", "totask2", totask2Projects.get(0).getName());
        assertEquals("totask2 not id 1", 1, totask2Projects.get(0).getId());
    }
    
    /** find totask2 project inserted by data.sql. */
    @Test   
    public void testReadTotask2ProjectWithLeads() {
        List<Project> totask2Projects = projectRepository.findByName("totask2");
        assertEquals("project not found", 1, totask2Projects.size());
        Project p = totask2Projects.get(0);
        LOG.debug("found totask2 project: " + p);
        assertNotNull("project leads set null", p.getProjectLeads());
        assertTrue("0 or more leads" , p.getProjectLeads().size() >= 0);
        
        LOG.debug("found: " + p + 
                " with leads: " + 
                            p.getProjectLeads().stream()
                            .map(User::getUsername)
                            .collect(Collectors.joining(", "))
        );
   }
    
    
    /** save, getOne. */
    @Test   
    public void testStoreProjectWithLead() {
        
        User admin = userRepository.getOne(TestConstants.ADMIN_USER);
        User user  = userRepository.getOne(TestConstants.TEST_USER);
        
        Project project = new Project();
        project.setName("junit tests project with lead");
        project.getProjectLeads().add(admin);
        project.getProjectLeads().add(user);
        
        Project savedProject = projectRepository.saveAndFlush(project);
        LOG.debug("saved new project " + savedProject + " id:" + savedProject.getId());
        
        assertTrue("id for project generated" , savedProject.getId() > 0);
        assertTrue("new project with id exists", projectRepository.exists(savedProject.getId()));        
        
        Project refetchedProject = projectRepository.getOne(savedProject.getId());
        assertEquals("name stored", project.getName() , refetchedProject.getName());
        
        assertEquals("2 leads", 2, refetchedProject.getProjectLeads().size());        
        assertTrue("admin as lead", refetchedProject.getProjectLeads().stream().anyMatch(u -> u.getId() == TestConstants.ADMIN_USER));
        assertTrue("user as lead", refetchedProject.getProjectLeads().stream().anyMatch(u -> u.getId() == TestConstants.TEST_USER));
        
        projectRepository.delete(refetchedProject);
    }
        
}

