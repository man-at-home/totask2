package org.regele.totask2.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


/** data access layer for work entries. */
public interface WorkEntryRepository extends JpaRepository<WorkEntry, Long>  {

    /** find all tasks for the given project. */
    @Query("select we from WorkEntry we where we.user.id = :userId and we.at = :at order by we.task.id asc, we.id desc")
    List<WorkEntry> findForUserAndDay(@Param("userId") long userId, @Param("at") Date at);    
}
