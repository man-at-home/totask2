package org.manathome.totask2.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** not allowed. */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "not allowed")
public class NotAllowedException extends ToTaskExceptionBase {

    private static final long serialVersionUID = 1L;

    public NotAllowedException() {
    }


    public NotAllowedException(String message) {
        super(message);
    }
}
