package org.manathome.totask2.model;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;


import org.manathome.totask2.util.AAssert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// tag::developer-manual-model[] 

/** 
 * a working task of a {@link Project} that needs to be worked on. 
 * 
 * @author man-at-home
 */
@Entity                                                     // <1>
@Audited
@Table(name = "TT_TASK")
@AuditTable("TT_TASK_HISTORY")
public class Task {
   
    @Id                                                     // <2>
    @GeneratedValue(strategy = GenerationType.AUTO)  
    @Column(name = "ID") 
    private long     id;            

    @Size(min = 2, max = 250)
    @NotNull
    @Column(name = "NAME", nullable = false, length = 250)  // <3>
    private String  name;
    
// end::developer-manual-model[] 
    
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "PROJECT_ID", referencedColumnName = "ID",
                foreignKey = @ForeignKey(name = "FK_TT_TASK_OWNING_PARENT") 
    )
    private Project project;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    private Collection<TaskAssignment> taskAssignments = new ArrayList<TaskAssignment>();     
   
    /** ctor. */
    public Task() {}
    
    /** create new task. */
    Task(@NotNull final Project project) {
        this();
        this.project = AAssert.checkNotNull(project);
    }
    
    /** display name of this task. */
    public String getName() { 
        return name; 
    }
    
    /** change task name. */
    public void setName(@NotNull final String name) { 
        this.name = name; 
    }  
    
    /** pk (task.id). 0 with new, unsaved tasks. */
    public long getId() { 
        return id; 
    }    
    
    /** change id. */
    protected void setId(long id) { 
        this.id = id; 
    }
    
    /** project owning this task. */
    public Project getProject() { 
        return project; 
    }
    
    /** change project parent. */
    public void setProject(final Project project) { 
        this.project = project; 
    }

    
    /** all {@link User} assignments for this task. */
    public Stream<TaskAssignment> getAssignments() { 
        return this.taskAssignments.stream(); 
    }
    
    /** create new assignment for this task. */
    public TaskAssignment addAssignment(User user) {
        TaskAssignment ta = new TaskAssignment(this, user);
        this.taskAssignments.add(ta);
        return ta;
    }    
    
    /** remove assignment of {@link User} from this task. */
    public void removeAssignment(TaskAssignment ta) {
        this.taskAssignments.remove(ta);
    }
    
    
    /** access check (edit allowed for admin and project leads). */
    public boolean isEditAllowed(final User user) {
        return user != null &&
               (user.isAdmin() || this.getProject().isEditAllowed(user));
    }
    
    
    /** debug. */
    @Override
    public String toString() {
        return "Task[" + id + ", name:" + name + "]";
    }
}
