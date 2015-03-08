package org.manathome.totask2.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/** data access layer for tasks. */
public interface TaskRepository extends JpaRepository<Task, Long>  {

    /** find tasks with specified name. */
    List<Task> findByName(String name);
    
    /** find all tasks for the given project. */
    @Query("select t from Task t where t.project.id = :projectId order by t.id asc")
    List<Task> findByProjectId(@Param("projectId") long projectId);
    
}
