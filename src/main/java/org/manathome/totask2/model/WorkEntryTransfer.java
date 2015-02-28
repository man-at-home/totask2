package org.manathome.totask2.model;

import java.util.Date;

/** 
 * data resulting out of WorkEntry json serializing.
 * 
 * @see    WorkEntry
 * @author man-at-home
 * @since  2015-02-28
 */
public class WorkEntryTransfer {
    
    private long    id;
    private Date    at;
    private float   duration;
    private String  taskName;
    private long    taskId;
    private String  comment;
    
    /**
     * @return the id
     */
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
    public final Date getAt() {
        return at;
    }
    /**
     * @param at the at to set
     */
    public final void setAt(Date at) {
        this.at = at;
    }
    /**
     * @return the duration
     */
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
    public final long getTaskId() {
        return taskId;
    }
    /**
     * @param taskId the taskId to set
     */
    public final void setTaskId(long taskId) {
        this.taskId = taskId;
    }
    
    public String getComment() {
        return comment;
    }
    
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
