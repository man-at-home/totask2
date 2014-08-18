package org.regele.totask2.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/** data access layer. */
public interface TaskRepository extends JpaRepository<Task, Long>  {

    List<Task> findByName(String name);
    
}
