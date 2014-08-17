package org.regele.totask2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/** project to log work on tasks. */
@Entity
@Table(name = "TT_PROJECT")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)    
    private long     id;
    
    @Column(nullable = false)
    private String name;
   
    /** debug.*/
    @Override
    public String toString() {
        return "Project [name=" + name + "]";
    }

    /** display name of this project. */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
 

}
