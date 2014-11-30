package org.regele.totask2.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


/** data access layer for taskAssignments. */
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Long>  {

    /** all assignments for given task. used in admin ui. */
    List<TaskAssignment> findByTask(Task task);
    
    /** find all tasks for the given project that could be worked on at least one day in given timespan from-until. 
     * used on weekEntry page.
     * */
    @Query("select ta from TaskAssignment ta where ta.user.id = :userId and ta.startingFrom <= :until and ( ta.until is null or ta.until >= :from )")    
    List<TaskAssignment> findByUserAndPeriod
            (
                @Param("userId") long userId, 
                @Param("from") Date from, 
                @Param("until") Date until
            );   
    
}
