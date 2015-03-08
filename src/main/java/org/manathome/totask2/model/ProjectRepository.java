package org.manathome.totask2.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/** data access layer. 
 * 
 * @author man-at-home
 * */
public interface ProjectRepository extends JpaRepository<Project, Long>  {

    /** find project by its name. */
    List<Project> findByName(String name);
    
}
