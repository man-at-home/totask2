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
import org.springframework.test.context.web.WebAppConfiguration;

import com.wordnik.swagger.config.SwaggerConfig;

import java.util.List;
import java.util.stream.Collectors;


/** testing db access. */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, SwaggerConfig.class })
@WebAppConfiguration
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
        LOG.debug("found " + userRepository.count() + " users in db.");
        userRepository.findAll().stream().forEach( u -> LOG.debug("user in db: " + u));
        assertTrue("user table access not possible" ,  userRepository.count() >= 1);
    }

    /** save, getOne. */
    @Test   
    public void testStoreUser() {
        User user = new User();
        user.setDisplayName("junit tests testStoreUsers");
        user.setUsername("testStoreUsers");
        user.setActive(true);
        
        User savedUser = userRepository.save(user);
        LOG.debug("Saved new user " + savedUser + " id:" + savedUser.getId());
        
        assertTrue("id for user generated" , savedUser.getId() > 0);
        assertTrue("new user id exists", userRepository.exists(savedUser.getId()));
        // assertTrue(" user table access not possible" ,  userRepository. >= 0);
        
        User refetchedUser = userRepository.getOne(savedUser.getId());
        assertEquals("display name stored", user.getDisplayName() , refetchedUser.getDisplayName());
        assertEquals("user name stored", user.getUsername() , refetchedUser.getUsername());
        assertEquals("user active stored", user.isActive() , refetchedUser.isActive());
        
        List<User> foundUser = userRepository.findByUserName(user.getUsername());
        assertEquals("user found by username " + user.getUsername(), 1, foundUser.size());
    }
    
    /** save, delete, findOne. */
    @Test   
    public void testStoreAndDeleteUser() {
        User user = new User();
        user.setDisplayName("junit tests user testStoreAndDeleteUser");
        user.setUsername("testStoreAndDeleteUser");
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
        assertEquals("admin name not admin", adminUsers.get(0).getUsername(), "admin");
        assertTrue("admin not active", adminUsers.get(0).isActive());
    }   

    /** find unit-test-user user inserted by data.sql. */
    @Test   
    public void testReadTestUser() {
        List<User> testUsers = userRepository.findByUserName("unit-test-user");
        if( testUsers.size() != 1)
        {
            String s = "NOn unit-test-user FOUND in [";
            s += userRepository.findAll().stream().map( User::getUsername ).collect(Collectors.joining(", ")) + "]";
            fail(s);
        }
    }   
    
    
}

