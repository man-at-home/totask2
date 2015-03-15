package org.manathome.totask2.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


/** data access layer for work entries. 
 * 
 * @author man-at-home
 * */
public interface WorkEntryRepository extends JpaRepository<WorkEntry, Long>  {

    /** find all done work an a task. */
    @Query("select we from WorkEntry we where we.task.id = :taskId order by we.id desc")
    List<WorkEntry> findForTask(@Param("taskId") long taskId);   
     
    
    /** find all tasks for the given project and day. */
    @Query("select we from WorkEntry we where we.user.id = :userId and we.at = :at order by we.task.id asc, we.id desc")
    List<WorkEntry> findForUserAndDay(@Param("userId") long userId, @Param("at") Date at);   
    
    /** find all tasks for the given project duration. */
    @Query("select we from WorkEntry we where we.user.id = :userId and we.at >= :from and we.at <= :until order by we.task.id asc, we.at asc")
    List<WorkEntry> findForUserAndTimespan(@Param("userId") long userId, @Param("from") Date from, @Param("until") Date until);      
}
