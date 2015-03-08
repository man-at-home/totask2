package org.manathome.totask2.util;

/** runtime totask2 exception for base service errors. */
public class EnvironmentException extends ToTaskExceptionBase {

    private static final long serialVersionUID = 1L;

    /** ctor. */
    public EnvironmentException() {
        super();
    }

    /** ctor. */
    public EnvironmentException(final String message) {
        super(message);
    }

    /** ctor. */
    public EnvironmentException(final String message, final Throwable cause) {
        super(message, cause);
    }    
}
