package org.manathome.totask2.model;


import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.manathome.totask2.util.AAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


/** 
 * project to log work on tasks. 
 * 
 * <p>
 * <br>
 * <img alt="project-uml" src="doc-files/totask2.design.datamodel.project.png">
 * </p>
 * @see Task 
 * @see User User as project lead  
 * @author man-at-home
 * 
 */
@Entity
/*
@startuml doc-files/totask2.design.datamodel.project.png

 Project         "1" *-- "n" Task            : contains
 Project         "n"  -- "n" User            : leads 

 Project   : id           : PK
 Project   : name         : String
 Project   : isAdmin      : bool
 Project   : projectLeads : Set<User>
 Project   : tasks        : Collection<Tasks>
 
 @enduml
 */
//tag::developer-manual-history-entity[]
@Table(name = "TT_PROJECT")
@Audited                                    // <1>
@AuditTable("TT_PROJECT_HISTORY")           // <2>
@ApiModel(value = "Project", description = "project to log work on tasks, containing *task*s and being administered by project leads")
public class Project {

    private static final Logger LOG = LoggerFactory.getLogger(Project.class);  
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  
    @Column(name = "ID")
    private long     id;
	
//end::developer-manual-history-entity[]
    
    @NotNull    
    @Size(min = 2, max = 250)
    @Column(name = "NAME", nullable = false, length = 250)
    private String name;

   
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private Collection<Task> tasks = new ArrayList<Task>(); 

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> projectLeads = new HashSet<User>(0);
        
    // ============================================================================
    
    
    /** unique id of given project (PK). */
    @ApiModelProperty(value = "unique id of given project (PK).")
    public long getId() { 
        return this.id; 
    }
    
    /** setting the pk, shall not be called directly. */
    public void setId(long id) { 
        AAssert.checkZero(this.id, "project id already set");
        this.id = id; 
    }
    
    /** leads of this projects. These {@link User}s are allowed to create and administer {@link Task}s for this project. 
     *
     * @see User the project lead is a user.
     * @see Task leads are allowed to administer tasks.
     * 
     * @since 2014-11-11
     */
    @JsonIgnore
    public Set<User> getProjectLeads() { 
        return this.projectLeads; 
    }
    /** change leads. */
    public void setProjectLeads(final Set<User> projectLeads) { 
        this.projectLeads = projectLeads; 
    }
    
    /** all {@link Task}s belonging to this project. */
    @JsonIgnore
    public Stream<Task> getTasks() { 
        return this.tasks.stream(); 
    }
    
    /** create new {@link Task} below this project. */
    public Task createTask() {
        Task t = new Task(this);
        this.tasks.add(t);
        return t;
    }
        

    /** display name of this project, length: 2..250. */
    @ApiModelProperty(value = "display name of this project, length: 2..250", required = true)
    public String getName() { 
        return name; 
    }
    
    /** change name of project. */
    public void setName(@NotNull final String name) { 
        this.name = name; 
    }

    
    
    /** access check (edit allowed for admin and project leads). */
    public boolean isEditAllowed(final User user) {
        return user != null &&
               (user.isAdmin() || this.getProjectLeads().stream().anyMatch(pl -> pl.equals(user)));
    }
    
    
    /** debug.*/
    @Override
    public String toString() {
        return "Project [" + this.id  + ", name=" + this.name + "]";
    }
    
    
    
    /** internal logging helper. */
    public void dump() {
        dump(this);
    }
    
    
    /** internal logging helper. */
    public static void dump(final Project project) {
        
        if (project == null) {
            LOG.debug("no project to dump");
        } else {

            LOG.debug("project........:  " + project.toString());
            LOG.debug(".......leads...:  " + 
                                (
                                project.getProjectLeads() == null ? 
                                        "" :
                                project.getProjectLeads().stream()
                                .map(User::getUsername)
                                .collect(Collectors.joining(", "))
                                )
                     );
           
        }        
    }
}
