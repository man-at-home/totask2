package org.regele.totask2.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/** project to log work on tasks. */
@Entity
@Table(name = "TT_PROJECT")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  
    @Column(name = "ID")
    private long     id;
  
    @Size(min = 2, max = 250)
    @NotNull    
    @Column(name = "NAME", nullable = false, length = 250)
    private String name;
   
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private Collection<Task> tasks = new ArrayList<Task>();    

    /** pk. */
    public long getId() { return this.id; }
    public void setId(long id) { this.id = id; }
    
    /** all tasks belonging to this project. */
    public Stream<Task> getTasks() { return this.tasks.stream(); }
    
    public void addTask(Task task) {
        if (task.getProject() != this) {
            throw new IllegalStateException("task " + task + " not for project " + this);
        }
        this.tasks.add(task);
    }    

    /** display name of this project. */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
 
    /** debug.*/
    @Override
    public String toString() {
        return "Project [" + this.id  + ", name=" + this.name + "]";
    }


}
