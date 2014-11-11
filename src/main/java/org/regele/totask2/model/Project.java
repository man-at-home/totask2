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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


/** project to log work on tasks. */
@Entity
@Table(name = "TT_PROJECT")
@ApiModel(value="Project", description="project to log work on tasks, containing *task*s and being administered by project leads")
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
    
    /** unique id of given project (PK). */
    @ApiModelProperty(value="unique id of given project (PK).")
    public long getId() { return this.id; }
    public void setId(long id) { this.id = id; }
    
    /** all tasks belonging to this project. */
    @JsonIgnore
    public Stream<Task> getTasks() { return this.tasks.stream(); }
    
    /** create new task in project. */
    public Task createTask() {
        Task t = new Task(this);
        this.tasks.add(t);
        return t;
    }
        

    /** display name of this project, length: 2..250. */
    @ApiModelProperty(value="display name of this project, length: 2..250", required=true)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
 
    /** debug.*/
    @Override
    public String toString() {
        return "Project [" + this.id  + ", name=" + this.name + "]";
    }
    

}
