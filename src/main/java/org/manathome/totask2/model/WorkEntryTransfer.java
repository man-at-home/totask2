package org.manathome.totask2.model;

import org.manathome.totask2.util.AAssert;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
 * data resulting out of WorkEntry json serializing (transfer via json to mobile app).
 * 
 * @see    WorkEntry
 * @author man-at-home
 * @since  2015-02-28
 */
public class WorkEntryTransfer implements Serializable{
    
    /** serialization. */
    private static final long serialVersionUID = 1L;
    
    private long    id;
    private Date    at;
    private float   duration;
    private String  taskName;
    private long    taskId;
    private String  comment;
    
    /**
     * @return the id (0 for new entries).
     */
    @ApiModelProperty(value = "workEntry id, required for update, 0 for insert", required = false)
    public final long getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public final void setId(long id) {
        this.id = id;
    }
    /**
     * @return the at
     */
    @ApiModelProperty(value = "date of work entry", required = true)
    public final Date getAt() {
        return at == null ? null : new Date(at.getTime());
    }
    /**
     * @param at the at to set
     */
    public final void setAt(@NotNull final Date at) {
        this.at = new Date(AAssert.checkNotNull(at.getTime()));
    }
    /**
     * @return the duration
     */
    @ApiModelProperty(value = "duration of work in hours", required = true)
    public final float getDuration() {
        return duration;
    }
    /**
     * @param duration the duration to set
     */
    public final void setDuration(float duration) {
        this.duration = duration;
    }
    
    /**
     * @return the taskName
     */
    @ApiModelProperty(value = "task.name, not required for save", required = false)
    public final String getTaskName() {
        return taskName;
    }
    
    /**
     * @param taskName the taskName to set
     */
    public final void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    /**
     * @return the taskId
     */
    @ApiModelProperty(value = "task.id, required for save", required = true)
    public final long getTaskId() {
        return taskId;
    }
    
    /**
     * task to work on.
     * 
     * @param taskId the taskId to set
     */
    public final void setTaskId(long taskId) {
        this.taskId = taskId;
    }
    
    /** optional comment. */
    @ApiModelProperty(value = "optional comment", required = false)
    public String getComment() {
        return comment;
    }
    
    /**
     * set optional comment.
     * 
     * @param comment text
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    /** debug output.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "WorkEntryTransfer [id=" + id + ", at=" + at + ", duration="
                + duration + ", taskName=" + taskName + ", taskId=" + taskId
                + ", comment=" + comment + "]";
    }
      
}
