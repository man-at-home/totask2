package org.manathome.totask2.util;

/** abstract runtime exception base class for all totask2 exceptions. */
public abstract class ToTaskExceptionBase extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** ctor base. */
    public ToTaskExceptionBase() {
        super();
     }

    /** ctor base. */
    public ToTaskExceptionBase(String message, Throwable cause) {
        super(message, cause);
    }

    /** ctor base. */
    public ToTaskExceptionBase(String message) {
        super(message);
    }
}
