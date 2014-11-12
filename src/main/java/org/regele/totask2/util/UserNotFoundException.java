package org.regele.totask2.util;


public class UserNotFoundException extends ToTaskExceptionBase {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }

}
