/**
 * 
 */
package org.manathome.totask2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

/**
 * helper to check roles of logged in user.
 * 
 * @see org.manathome.totask2.model.User
 * 
 * @author man-at-home
 */
public class Authorisation {

    private static final Logger LOG = LoggerFactory.getLogger(Authorisation.class);
    
    
    /** User.isAdmin. */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    
    /** 
     * user has admin role. 
     * 
     * @see #ROLE_ADMIN
     * */
    public static boolean isAdmin(final Authentication auth)  {
        
        boolean isAdmin = 
                auth != null && 
                auth.isAuthenticated() &&
                auth.getAuthorities().stream().anyMatch(a -> ROLE_ADMIN.equals(a.getAuthority()))  
                ;
        
        LOG.debug("Authentication checked: user " + auth + " is admin:" + isAdmin);
        return isAdmin;
    }

    /** 
     * check required flag requiredOption for access. 
     * 
     * @exception NotAllowedException
     * */
    public static void require(final boolean requiredOption) {
        
        if (!requiredOption) {
            throw new NotAllowedException("not allowed");
        }
    }
    
}
