package org.regele.totask2.model;

import java.util.Arrays;

/**
 * Holder, one week of workEntries for a specific task.
 * 
 * @author man-at-home 
 */
public class TaskInWeek {

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((task == null) ? 0 : new Long(task.getId()).hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){       
            return true;
        }
        if (obj == null){ 
            return false;
        }    
        if (getClass() != obj.getClass()){ 
            return false;
        }
        TaskInWeek other = (TaskInWeek) obj;
        if (task == null) {
            if (other.task != null) {
                return false;
            }
        } else if (task.getId() != other.task.getId()){
            return false;
        }
        return true;
        
        // TBD: compare date ranges
    }


    /** all working hours for this task. */
    private Task task = null;

    /** max seven slots a week for a given task. */
    private WorkEntry[] dailyEntries = null;

    public TaskInWeek(Task task) {
        this.task = task;
        this.dailyEntries = new WorkEntry[7];
    }    
    
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public WorkEntry[] getDailyEntries() {
        return dailyEntries;
    }

    public void setDailyEntries(WorkEntry[] dailyEntries) {
        this.dailyEntries = dailyEntries;
    }
    
    public double getDuration() {
        
        if (this.dailyEntries == null) { 
           return 0;       
        }
        else {
           double sum = 0;
           for (WorkEntry we : this.dailyEntries) {
               sum += we.getDuration();
           }
           return sum;        
        }
    }
    
    public boolean isModifiedByUser() {
        
        if( this.getDailyEntries() == null)
            return false;
        else
            return Arrays.stream( this.getDailyEntries() ).anyMatch( de -> de.isModifiedByUser());
    }
    

    @Override
    public String toString() {
        return "TaskInWeek [task=" + task + "]";
    }
}
