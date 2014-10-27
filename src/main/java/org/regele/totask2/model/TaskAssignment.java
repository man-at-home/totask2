package org.regele.totask2.model;

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

import org.regele.totask2.util.ApplicationAssert;
import org.regele.totask2.util.LocalDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * mapping user to task he should work on.
 * 
 * @author Manfred
 * @since  2014-10-26 
 */
@Entity
@Table(name = "TT_TASK_ASSIGNMENT")
public class TaskAssignment {
       
    private static final @NotNull Logger LOG = LoggerFactory.getLogger(TaskAssignment.class);      

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
    private Date startingFrom;


    @Column(name = "until", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date until;
    

    public TaskAssignment() {
        this.startingFrom = LocalDateConverter.toDate( LocalDate.now() );
    }

    /** give user new assignment. */
    public TaskAssignment(@NotNull final Task task, @NotNull final User user) {
        this();
        setUser(user);
        setTask(task);
        LOG.debug("created " + this.toString());
    }
    
    
    /* pk. */
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    /** working user. */
    public @NotNull User getUser() { return this.user; }
    protected void setUser(final @NotNull User user) { this.user = user; }

    /** assigned task for user. */
    public @NotNull Task getTask() { return this.task; }
    protected void setTask(final @NotNull Task task) { this.task = task; }
    
    /** working on this task is allowed beginning from "from" for given user. */    
    public @NotNull LocalDate getFrom() { return this.startingFrom == null ? null : LocalDateConverter.toLocalDate( this.startingFrom ); }
    
    /** set from date, not null! */
    public void setFrom(@NotNull LocalDate from) 
    { 
        ApplicationAssert.assertNotNull("from must not be null", from);
        this.startingFrom = LocalDateConverter.toDate(from); 
    }
    
    /** working on this task is allowed until "until" for given user (may be null for "no limit"). */    
    public LocalDate getUntil() { return this.until == null ? null : LocalDateConverter.toLocalDate( this.until ); }
    public void setUntil(LocalDate until) { 
        this.until = until == null ? null : LocalDateConverter.toDate(until); 
    }
    
    /** debug output. */
    @Override
    public String toString() {
        return "Assignment [id=" + id + ": task " + task + ", to be worked on by=" + user + "]";
    }
        
}
