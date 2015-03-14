package org.manathome.totask2.model;

import org.manathome.totask2.util.AAssert;
import org.manathome.totask2.util.LocalDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wordnik.swagger.annotations.ApiModelProperty;


/**
 * logged working hours for one user for a given task.
 * 
 * @author  man-at-home
 * @since   2014-08-23 
 * @version 2015-02-07
 */
@Entity
@Table(name = "TT_WORKENTRY")
public class WorkEntry {
    
    private static final Logger LOG = LoggerFactory.getLogger(WorkEntry.class);  
    
    /** ctor. */
    public WorkEntry() {
        setAtDate(LocalDate.now());
    }


    /** ctor. */
    public WorkEntry(final User user, final Task task) {
        this();
        this.user = user;
        this.task = task;
    }
    
    /** ctor. */
    public WorkEntry(final User user, final Task task, final LocalDate dt) {
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
     * NON-PERSISTANT! 
     */
    @Transient
    private boolean isModifiedByUser = false;
    
    
    /** UI Helper: is for current user and entry further editing allowed (valid assignment exists?).
     * NON-PERSISTANT!
     */
    @Transient
    private boolean isEditable = true;
    

    /**
     * true: the user may change this entry (false if readonly). 
     * 
     * controls ui input/editables behavior weekEntry page for given work entry.
     */
    @JsonIgnore
    public boolean isEditable() {
        return isEditable;
    }


    /**
     * user may edit this entry.
    */
    public void setEditable(final boolean isEditable) {
        this.isEditable = isEditable;
    }


    /** pk. */
    @ApiModelProperty(value = "Id", required = true, notes = "primary key of given workEntry(may be 0 only if not saved yet.)")
    public long getId() {
        return id;
    }
    
    /** change pk. */
    public void setId(long id) {
        AAssert.checkZero(this.id, "workEntry.id already set");
        
        this.id = id;
    }

    /** optional comment (prose). */
    public String getComment() { 
        return comment; 
    }
    
    /** change comment. */
    public void setComment(final String comment) { 
        if (this.comment != null && !this.comment.equals(comment)) {
            isModifiedByUser = true;
        }
        this.comment = comment; 
    }

    /** employee working on a given task. */
    @JsonIgnore
    public User getUser() { 
        return user; 
    }
    
    /** change user. */
    public void setUser(final User user) { 
        this.user = user; 
    }

    /** task on which the work was done. */ 
    @JsonIgnore
    public Task getTask() { 
        return task; 
    }
    
    /** change task. */
    public void setTask(final Task task) { 
        this.task = task; 
    }

    /** task.name. */
    @ApiModelProperty(value = "TaskName", required = true, notes = "task.name of task worked on")    
    public String getTaskName() {
        return this.task == null ? "" : this.task.getName();
    }

    /** task.id. */
    @ApiModelProperty(value = "TaskId", required = true, notes = "task.id of task worked on")
    public long getTaskId() {
        return this.task == null ? 0L : this.task.getId();
    }
    
    
    /** date the work was done. 
     * 
     * needs an Jackson2ObjectMapperBuilder to be serialized properly as date.
     * 
     * @deprecated use modern java8 methods instead. 
     */
    @ApiModelProperty(value = "At", required = true, notes = "date of work (without time part). Required field.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // no effect unfortunately
    @JsonSerialize()
    @Deprecated
    public Date getAt() { 
        return new Date (at.getTime()); 
    }
    
    /** 
     * change date. 
     * 
     * @deprecated use {@link #setAtDate(LocalDate)} instead.
     * */
    @Deprecated
    public void setAt(final Date at) { 
        this.at = new Date(at.getTime()); 
    }
    
    /**
     * date the work was done. 
     * LocalDate -> Date. 
     */
    @JsonIgnore
    public LocalDate getAtDate() { 
        assert at != null;
        return LocalDateConverter.toLocalDate(this.at);
    }
    
    /** change date. */
    public void setAtDate(final LocalDate at) { 
        this.at = LocalDateConverter.toDate(at); 
    }

    /** duration of the logged work in hours. */
    @ApiModelProperty(value = "Duration", required = true, notes = "duration of work on this day and task. d >=0.")
    public float getDuration() { 
        return duration; 
    }
    
    /** change duration. returns this for method chaining. */
    public WorkEntry setDuration(final float duration) { 
        if (duration != this.duration) {
            LOG.debug("change duration: " + this.duration + " ->" + duration);
            isModifiedByUser = true;
        }
        this.duration = duration; 
        return this;
    }

    /** debug. */
    @Override
    public String toString() {
        return "WorkEntry [at:" + at + ", duration: " + duration + "h, task: " + getTaskId() + ", " + getTaskName() + "]";
    }    
    
    /** html hint output. */
    @JsonIgnore
    public String getTitleComment() {
        return 
                this.id + ": " + 
                LocalDateConverter.format(this.getAtDate())  + " " + 
                this.getComment() +
                (this.isEditable() ? "" : " (currently no active assignment)")
                ;
    }
    
    /** instance is modified ("dirty") therefore needs to be saved to db. */
    @JsonIgnore
    public boolean isModifiedByUser() {
        return this.isModifiedByUser;
    }
}
