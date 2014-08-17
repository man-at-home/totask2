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


/** testing db access. */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
public class UserRepositoryTest {
    

    private static final Logger LOG = LoggerFactory.getLogger(UserRepositoryTest.class);
    
    /** user repository under test. */
    @Autowired
    private UserRepository userRepository;
    
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LOG.debug("testing db user access ");
    }

    /** userRepository.count. */
    @Test   
    public void testReadUsers() {
        assertNotNull("user repo not injected", userRepository);
        assertTrue("user table access not possible" ,  userRepository.count() >= 0);
    }

    /** save, getOne. */
    @Test   
    public void testStoreUser() {
        User user = new User();
        user.setDisplayName("junit tests testStoreUsers");
        user.setUserName("testStoreUsers");
        user.setActive(true);
        
        User savedUser = userRepository.save(user);
        LOG.debug("Saved new user " + savedUser + " id:" + savedUser.getId());
        
        assertTrue("id for user generated" , savedUser.getId() > 0);
        assertTrue("new user id exists", userRepository.exists(savedUser.getId()));
        // assertTrue(" user table access not possible" ,  userRepository. >= 0);
        
        User refetchedUser = userRepository.getOne(savedUser.getId());
        assertEquals("display name stored", user.getDisplayName() , refetchedUser.getDisplayName());
        assertEquals("user name stored", user.getUserName() , refetchedUser.getUserName());
        assertEquals("user active stored", user.isActive() , refetchedUser.isActive());
    }
    
    /** save, delete, findOne. */
    @Test   
    public void testStoreAndDeleteUser() {
        User user = new User();
        user.setDisplayName("junit tests user testStoreAndDeleteUser");
        user.setUserName("testStoreAndDeleteUser");
        user.setActive(true);
        
        User savedUser = userRepository.save(user);
        LOG.debug("Saved new user " + savedUser + " id:" + savedUser.getId());
 
        long id = savedUser.getId();
        
        userRepository.delete(savedUser);
        User notToBeFoundUser = userRepository.findOne(id);
        assertNull("deleted user found", notToBeFoundUser);
    }

    /** find admin user inserted by data.sql. */
    @Test   
    public void testReadAdminUser() {
        List<User> adminUsers = userRepository.findByUserName("admin");
        assertEquals("admin user not found", adminUsers.size() , 1);
        LOG.debug("found admin user: " + adminUsers.get(0));
        assertEquals("admin name not admin", adminUsers.get(0).getUserName(), "admin");
        assertFalse("admin not inactive", adminUsers.get(0).isActive());
    }   
    
}

