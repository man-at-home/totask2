package org.regele.totask2.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/** data access layer. */
public interface UserRepository extends JpaRepository<User, Long>  {

    List<User> findByUserName(String userName);
    
}
