package org.manathome.totask2.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** task assignment does not exist. */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no such task assignment")  // 404
public final class TaskAssignmentNotFoundException extends ToTaskExceptionBase {

    private static final long serialVersionUID = 1L;

    /** ctor. */
    public TaskAssignmentNotFoundException() {
        super();
    }

    /** ctor. */
    public TaskAssignmentNotFoundException(String message) {
        super(message);
    }

}
