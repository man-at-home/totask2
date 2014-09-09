package org.regele.totask2.model;

import org.regele.totask2.util.LocalDateConverter;

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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * logged working hours for one user for a given task.
 * 
 * @author Manfred
 * @since  2014-08-23
 */
@Entity
@Table(name = "TT_WORKENTRY")
public class WorkEntry {
    
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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID",
                foreignKey = @ForeignKey(name = "FK_TT_WORKENTRY_USER") 
    )   
    private User user;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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

    /** pk. */
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    /** optional comment (prose). */
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

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
    public Date getAt() { return at; }
    @Deprecated
    public void setAt(Date at) { this.at = at; }
    
    /**
     * date the work was done. 
     * LocalDate -> Date. 
     */
    public LocalDate getAtDate() { 
        assert at != null;
        return LocalDateConverter.toLocalDate(this.at);
    }
    
    public void setAtDate(final LocalDate at) { this.at = LocalDateConverter.toDate(at); }

    /** duration of the logged work in hours. */
    public float getDuration() { return duration; }
    public void setDuration(final float duration) { this.duration = duration; }


    @Override
    public String toString() {
        return "WorkEntry [at=" + at + ", duration=" + duration + "]";
    }    
    
    
}
