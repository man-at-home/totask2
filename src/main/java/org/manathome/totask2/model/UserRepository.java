package org.manathome.totask2.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/** data access layer. 
 * 
 * @see    User
 * @author man-at-home
 * */
public interface UserRepository extends JpaRepository<User, Long>  {

    /** fine user by name. */
    List<User> findByUserName(String userName);
    
}
