package org.regele.totask2.util;

public abstract class ToTaskExceptionBase extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ToTaskExceptionBase() {
        super();
     }

    public ToTaskExceptionBase(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ToTaskExceptionBase(String message, Throwable cause) {
        super(message, cause);
    }

    public ToTaskExceptionBase(String message) {
        super(message);
    }

    public ToTaskExceptionBase(Throwable cause) {
        super(cause);
    }

}
