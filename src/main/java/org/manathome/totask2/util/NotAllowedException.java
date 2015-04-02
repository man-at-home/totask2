package org.manathome.totask2.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** not allowed 403 (security). */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "not allowed")
public class NotAllowedException extends ToTaskExceptionBase {

    private static final long serialVersionUID = 1L;

    /** ctor. */
    public NotAllowedException() {}

    /** ctor. */
    public NotAllowedException(String message) {
        super(message);
    }
}
