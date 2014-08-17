package org.regele.totask2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** a working task of a project that needs to be worked on. */
@Entity
@Table(name = "TT_TASK")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)    
    private long     id;    
    
    @Column(nullable = false)
    private String  name;
    
    /** display name of this task. */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    /** debug. */
    @Override
    public String toString() {
        return "Task [name=" + name + "]";
    }
}
