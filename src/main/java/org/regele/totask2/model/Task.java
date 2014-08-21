package org.regele.totask2.model;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** a working task of a project that needs to be worked on. */
@Entity
@Table(name = "TT_TASK")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  
    @Column(name = "ID")
    private long     id;    

    @Size(min = 2, max = 250)
    @NotNull
    @Column(name = "NAME", nullable = false, length = 250)
    private String  name;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROJECT_ID", referencedColumnName = "ID",
                foreignKey = @ForeignKey(name = "FK_TT_TASK_OWNING_PARENT") 
    )
    private Project project;
      
    public Task() {}
    
    /** create new task. */
    Task(Project project) {
        this();
        this.project = project;
    }
    
    /** display name of this task. */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }  
    
    /* pk. */
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    /** project owning this task. */
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    
    /** debug. */
    @Override
    public String toString() {
        return "Task[" + id + ", name:" + name + "]";
    }
}
