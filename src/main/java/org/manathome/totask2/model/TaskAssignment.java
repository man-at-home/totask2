package org.manathome.totask2.model;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.manathome.totask2.util.AAssert;
import org.manathome.totask2.util.LocalDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;


/**
 * mapping a {@link User} to a {@link Task} he should work on.
 * 
 * <p>
 *  <br>
 *  <img alt="project-uml" src="doc-files/totask2.design.datamodel.taskAssignment.png">
 * </p>
 * @author  man-at-home
 * @since   2014-10-26 
 * @version 2015-02-07
 */
@Entity
/*
@startuml doc-files/totask2.design.datamodel.taskAssignment.png

 User            "1" -- "n" TaskAssignment  : may work on
 TaskAssignment  "n" -- "1" Task            : assigned to 

 TaskAssignment : from    : Date
 TaskAssignment : until   : Date
 
 @enduml
 */
@Audited
@Table(name = "TT_TASK_ASSIGNMENT")
@AuditTable("TT_TASK_ASSIGNMENT_HISTORY")
public class TaskAssignment {
       
    private static final Logger LOG = LoggerFactory.getLogger(TaskAssignment.class);      

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  
    @Column(name = "ID")
    private long     id;    

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "TASK_ID", referencedColumnName = "ID",
                foreignKey = @ForeignKey(name = "FK_TT_ASSIGNMENT_TASK") 
    )
    private Task task;    

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID",
                foreignKey = @ForeignKey(name = "FK_TT_ASSIGNMENT_USER") 
    )
    private User user;      
        
    @NotNull
    @Column(name = "startingFrom", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = ISO.DATE)
    private Date startingFrom;


    @Column(name = "until", nullable = true)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = ISO.DATE)
    private Date until;
    

    public TaskAssignment() {
        this.startingFrom = LocalDateConverter.toDate(LocalDate.now());
    }

    /** give user new assignment. */
    public TaskAssignment(@NotNull final Task task, @NotNull final User user) {
        this();
        setUser(user);
        setTask(task);
        setFrom(LocalDate.now());
        LOG.debug("created " + this.toString());
    }
    
    
    /* pk. */
    public long getId() { 
        return id; 
    }
    public void setId(long id) { 
        this.id = id; 
    }

    /** working user. */
    @NotNull public User getUser() { 
        return this.user; 
    }
    
    public void setUser(@NotNull final  User user) { 
        LOG.debug("assigning task to user: " + user);
        this.user = user; 
    }

    /** assigned task for user. */
    public Task getTask() { 
        return this.task; 
    }
    public void setTask(final Task task) { 
        LOG.debug("assigning task: " + task);
        this.task = task; 
    }
    
    
    public Date getStartingFrom() { 
        return this.startingFrom == null ? null : new Date(this.startingFrom.getTime()); 
    }
    
    public void setStartingFrom(@NotNull final Date from) { 
        this.startingFrom = new Date(AAssert.checkNotNull(from).getTime()); 
    }
    
    /** working on this task is allowed beginning from "from" for given user. */    
    @NotNull public LocalDate getFrom() { 
        return this.startingFrom == null ? 
                    null : 
                    LocalDateConverter.toLocalDate(this.startingFrom); 
    }
    
    /** set from date, not null! */
    public void setFrom(@NotNull final LocalDate from) { 
        this.startingFrom = LocalDateConverter.toDate(AAssert.checkNotNull(from, "from must not be null")); 
    }
    
    @DateTimeFormat(iso = ISO.DATE)
    public Date getValidUntil() { 
        return this.until == null ? null : new Date(this.until.getTime()); 
    }
    
    @DateTimeFormat(iso = ISO.DATE)
    public void setValidUntil(final Date until) { 
        this.until = until == null ? null : new Date(until.getTime());
    }    
    
    /** working on this task is allowed until "until" for given user (may be null for "no limit"). */    
    public LocalDate getUntil() { 
        return this.until == null ? 
                null : 
                LocalDateConverter.toLocalDate(this.until); 
    }
    
    public void setUntil(LocalDate until) { 
        this.until = until == null ? null : LocalDateConverter.toDate(until); 
    }

    /** check if given date is between from and until. */
    public boolean isInRange(@NotNull final LocalDate d) {
        
        return  d != null &&
                getFrom() != null &&
                !d.isBefore(getFrom()) &&
                (getUntil() == null || getUntil().isAfter(d));
    }    
    
    /** debug output. */
    @Override
    public String toString() {
        return "Assignment [id=" + id + ": task " + task + ", to be worked on by=" + user + " from " + startingFrom + " - " + until + "]";
    }


        
}
