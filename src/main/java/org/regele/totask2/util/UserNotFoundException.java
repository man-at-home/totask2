package org.regele.totask2.util;

/** 
 * user not found ui/http exception. 
 * 
 * @see     org.regele.totask2.model.User
 * @author  man-at-home
 */
public class UserNotFoundException extends ToTaskExceptionBase {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }

}
