package org.manathome.totask2.util;

/** base class. */
public abstract class ToTaskExceptionBase extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** ctor base. */
    public ToTaskExceptionBase() {
        super();
     }

    /** ctor base. */
    public ToTaskExceptionBase(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /** ctor base. */
    public ToTaskExceptionBase(String message, Throwable cause) {
        super(message, cause);
    }

    /** ctor base. */
    public ToTaskExceptionBase(String message) {
        super(message);
    }

    /** ctor base. */
    public ToTaskExceptionBase(Throwable cause) {
        super(cause);
    }

}
