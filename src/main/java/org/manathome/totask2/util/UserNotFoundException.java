package org.manathome.totask2.util;

/** 
 * user not found ui/http exception. 
 * 
 * @see     org.manathome.totask2.model.User
 * @author  man-at-home
 */
public class UserNotFoundException extends ToTaskExceptionBase {

    private static final long serialVersionUID = 1L;

    /** ctor. */
    public UserNotFoundException() {}

    /** ctor. */
    public UserNotFoundException(String message) {
        super(message);
    }

}
