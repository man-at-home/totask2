package org.regele.totask2.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



/**
 * User who can log his work here.
 * 
 * @author Manfred
 * @since  2015-08-17
 */
@Entity
@Table(name = "TT_USER", 
       indexes = {@Index(name = "IDX_TT_USER_NAME", unique = true, columnList = "USER_NAME")}
      )
public final class User {

    private static final Logger LOG = LoggerFactory.getLogger(User.class);    

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)    
    private long     id;
    
    @Size(min = 3, max = 50)
    @NotNull
    @Column(name = "USER_NAME", nullable = false, length = 50)
    private String userName;
    
    @Size(min = 3, max = 250)
    @NotNull
    @Column(name = "DISPLAY_NAME", nullable = false, length = 250)
    private String displayName;
    
    @Column(name = "ACTIVE", nullable = false)
    private boolean active;
    
    @Version    
    private long version;
    
    
    public User() {}

    /** internal pk. */
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    /** UserId used to login into this application.*/
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** display friendly name of this user, shown in dialogs. */
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /** may the given user login into this application? */
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (!active) {            
            LOG.debug("deactivating {0}" + this.userName);
        }
    }
    
    
    /** debug. */
    @Override
    public String toString() {
        return "User [userName=" + userName + ", active=" + active + "]";
    }  

}
