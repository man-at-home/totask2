package org.regele.totask2.model;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
 * User who can log his work into totask2 application.
 * 
 * spring-security: Provides core user information.
 * Implementations are not used directly by Spring Security for security purposes. They simply store user information 
 * which is later encapsulated into Authentication objects. This allows non-security related user information (such as email 
 * addresses, telephone numbers etc) to be stored in a convenient location. 
 * 
 * @author Manfred
 * @since  2015-08-17
 */
@Entity
@Table(name = "TT_USER", 
       indexes = {@Index(name = "IDX_TT_USER_NAME", unique = true, columnList = "USER_NAME")}
      )
public final class User implements UserDetails {

    private static final long serialVersionUID = 1L;

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
    
    @Column(name = "PASSWORD", nullable = true, length = 100)
    private String password;
    
    @Column(name = "IS_ADMIN", nullable = false)
    private boolean isAdmin;
    
    
    @Version    
    private long version;
    
    
    public User() {}

    /** internal pk. */
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    /** UserId used to login into this application.*/
    @Override
    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
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
    
    /** is it a totask2 admin? */
    public boolean isAdmin() {
        return this.isAdmin;
    }    

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    
    /** change password of given user. */
    public void changePasswor(String newUnencryptedPassword) {
        this.password = getPasswordEncoder().encode( newUnencryptedPassword );
    }
   
    
    /** debug. */
    @Override
    public String toString() {
        return "User [userName=" + userName + ", active=" + active + "]";
    }

    /** returns the authorities granted to the user.
     *  active rights/roles of user (currently only admin). */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
                
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
        
        if( this.isAdmin() ) {
            GrantedAuthority adminRole = new SimpleGrantedAuthority("ROLE_ADMIN");
            authorities.add(adminRole);
        }
        
        LOG.debug("authorities of user " + this.getDisplayName() + " requested: " + authorities.size());
        return authorities;
    }

    /** password, used by spring security. */
    @Override
    public String getPassword() {
        
        if( this.password == null || this.password.length() <= 0 )
        {
            LOG.debug("START-password of user " + this.getDisplayName() + " requested.");        
            return getPasswordEncoder().encode("123456");
        }
        else
        {
            LOG.debug("custom-password of user " + this.getDisplayName() + " requested.");        
            return this.password;
        }
    }

    /** Indicates whether the user's account has expired. */
    @Override
    public boolean isAccountNonExpired() {
        return this.isActive();
    }

    /** Indicates whether the user is locked or unlocked. */
    @Override
    public boolean isAccountNonLocked() {
        return this.isActive();
    }

    /** Indicates whether the user's credentials (password) has expired.*/
    @Override
    public boolean isCredentialsNonExpired() {
        return this.isActive();
    }

    /** Indicates whether the user is enabled or disabled. */
    @Override
    public boolean isEnabled() {
        return this.isActive();
    } 
    
    public static BCryptPasswordEncoder getPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

}
