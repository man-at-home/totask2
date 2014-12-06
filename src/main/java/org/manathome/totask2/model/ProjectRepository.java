package org.manathome.totask2.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/** data access layer. */
public interface ProjectRepository extends JpaRepository<Project, Long>  {

    List<Project> findByName(String name);
    
}
