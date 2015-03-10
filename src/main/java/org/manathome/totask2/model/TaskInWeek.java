package org.manathome.totask2.model;

import org.manathome.totask2.util.AAssert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Holder, one week of workEntries for a specific task.
 * 
 * @author man-at-home 
 */
public class TaskInWeek {

    /** hashCode. */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((task == null) ? 0 : Long.valueOf(task.getId()).hashCode());
        return result;
    }

    /** equals by taskId. */
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
    private ArrayList<WorkEntry> dailyEntries = null;

    /** .ctor. */
    public TaskInWeek(Task task) {
        this.task = task;
        this.dailyEntries = new ArrayList<WorkEntry>();
        for (int i = 0; i < 7; i++) {
            this.dailyEntries.add(null);    // simulate array behavior            
        }
    }    
    
    /** task to work on. */
    public Task getTask() {
        return task;
    }

    /** change task. */
    public void setTask(final Task task) {
        this.task = task;
    }
    
    /** set or replace entry. */
    public void setDailyEntry(int index, final WorkEntry we) {
        this.dailyEntries.set(AAssert.checkIndex(this.dailyEntries.size(), index), we);
    }
    
    /** get entry. */
    public WorkEntry getDailyEntry(int index) {
        return this.dailyEntries.get(AAssert.checkIndex(this.dailyEntries.size(), index));
    }

    /** get a copy of all entries. */
    public Stream<WorkEntry> getDailyEntries() {
        return this.dailyEntries.stream();
    }
    
    /** used in thymeleaf weekEntry.html template (does not work with java8-streams yet). */
    public List<WorkEntry> getIterableEntries() {
        return Collections.unmodifiableList(this.dailyEntries);
    }

    
    /** sum of working hours in this week (and task, user). */
    public double getDuration() {
        
        if (this.dailyEntries == null) { 
           return 0;       
        } else {
           double sum = 0;
           for (WorkEntry we : this.dailyEntries) {
               sum += we == null ? 0 : we.getDuration();
           }
           return sum;        
        }
    }
    
    /** any entry of this weeks work changed and is not yet persisted into database. */
    public boolean isModifiedByUser() {        
           return this.getDailyEntries().anyMatch(de -> de.isModifiedByUser());
    }
    

    /** debug. */
    @Override
    public String toString() {
        return "TaskInWeek [task=" + task + "]";
    }
}
