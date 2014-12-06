/**
 * 
 */
package org.regele.totask2.controller;


import org.regele.totask2.model.User;
import org.regele.totask2.service.UserCachingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;

import java.util.Collection;

/**
 * converting between Collections of {@link User} and user.id.
 * 
 * @see User
 * @see ProjectController used by ProjectController
 * @see UserCachingService internal use of UserCachingService
 * 
 * @author man-at-home
 *
 */
public class UserCollectionEditor extends CustomCollectionEditor {
    
    private static final Logger LOG = LoggerFactory.getLogger(UserCollectionEditor.class);
    
    private UserCachingService   userCachingService;
    
    /**
     * @param collectionType
     */
    @SuppressWarnings("rawtypes")
    public UserCollectionEditor(
            final Class<? extends Collection> collectionType, 
            final UserCachingService userCachingService) {
        super(collectionType);
        this.userCachingService = userCachingService;
    }

    
    protected Object convertElement(Object element) {
        if (element instanceof User) {
            LOG.debug("converting from User to User: " + element);
            return element;
        }
        if (element instanceof String) {
            LOG.debug("Looking up user by id " + element);
            User user = userCachingService.getCachedUserById(Long.parseLong(element + ""));
            return user;
        }
        LOG.error("Don't know what to do with: " + element);
        return null;    
    }
}
