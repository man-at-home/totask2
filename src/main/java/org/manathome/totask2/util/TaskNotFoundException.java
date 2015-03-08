package org.manathome.totask2.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** task does not exist. */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no such task")  // 404
public final class TaskNotFoundException extends ToTaskExceptionBase {

    private static final long serialVersionUID = 1L;

    /** ctor. */
    public TaskNotFoundException() {
        super();
    }

    /** ctor. */
    public TaskNotFoundException(String message) {
        super(message);
    }

}
