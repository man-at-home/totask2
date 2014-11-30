package org.regele.totask2.util;

/** runtime totask2 exception for base service errors. */
public class EnvironmentException extends ToTaskExceptionBase {

    private static final long serialVersionUID = 1L;

    public EnvironmentException() {
        super();
    }

    public EnvironmentException(final String message) {
        super(message);
    }

    public EnvironmentException(final String message, final Throwable cause) {
        super(message, cause);
    }    
}
