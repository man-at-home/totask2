package org.manathome.totask2.model;

import org.manathome.totask2.util.LocalDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * logged working hours for one user for a given task.
 * 
 * @author man-at-home
 * @since  2014-08-23 
 */
@Entity
@Table(name = "TT_WORKENTRY")
public class WorkEntry {
    
    private static final Logger LOG = LoggerFactory.getLogger(WorkEntry.class);  
    
    public WorkEntry() {
        setAtDate(LocalDate.now());
    }


    public WorkEntry(User user, Task task) {
        this();
        this.user = user;
        this.task = task;
    }
    
    public WorkEntry(User user, Task task, LocalDate dt) {
        this(user, task);
        this.setAtDate(dt);
    }    
    

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  
    @Column(name = "ID")
    private long     id;    

    @Size(max = 250)
    @Column(name = "COMMENT", nullable = true, length = 250)
    private String  comment;
    
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID",
                foreignKey = @ForeignKey(name = "FK_TT_WORKENTRY_USER") 
    )   
    private User user;
    
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "TASK_ID", referencedColumnName = "ID",
                foreignKey = @ForeignKey(name = "FK_TT_WORKENTRY_TASK")
    )
    private Task task;
    
    @NotNull
    @Column(name = "AT", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date at;
    
    @Min(0)
    @Max(24)
    @NotNull
    @Column(name = "DURATION", nullable = false)
    private float duration;
    
    /** some fields (duration, comment) can be edited on screen by user. changes are marked here.
     * NON-PERSISTANT! */
    @Transient
    private boolean isModifiedByUser = false;
    

    /** pk. */
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    /** optional comment (prose). */
    public String getComment() { return comment; }
    public void setComment(final String comment) { 
        if (this.comment != null && !this.comment.equals(comment)) {
            isModifiedByUser = true;
        }
        this.comment = comment; 
    }

    /** employee working on a given task. */
    public User getUser() { return user; }
    public void setUser(final User user) { this.user = user; }

    /** task on which the work was done. */ 
    public Task getTask() { return task; }
    public void setTask(final Task task) { this.task = task; }

    /** date the work was done. 
     * @deprecated use modern java8 methods instead. 
     */
    @Deprecated
    public Date getAt() { 
        return new Date (at.getTime()); 
    }
    @Deprecated
    public void setAt(final Date at) { 
        this.at = new Date(at.getTime()); 
    }
    
    /**
     * date the work was done. 
     * LocalDate -> Date. 
     */
    public LocalDate getAtDate() { 
        assert at != null;
        return LocalDateConverter.toLocalDate(this.at);
    }
    
    public void setAtDate(final LocalDate at) { 
        this.at = LocalDateConverter.toDate(at); 
    }

    /** duration of the logged work in hours. */
    public float getDuration() { 
        return duration; 
    }
    
    public void setDuration(final float duration) { 
        if (duration != this.duration) {
            LOG.debug("change duration: " + this.duration + " ->" + duration);
            isModifiedByUser = true;
        }
        this.duration = duration; 
    }

    /** debug. */
    @Override
    public String toString() {
        return "WorkEntry [at=" + at + ", duration=" + duration + "]";
    }    
    
    /** instance is modified ("dirty") therefore needs to be saved to db. */
    public boolean isModifiedByUser() {
        return this.isModifiedByUser;
    }
}
