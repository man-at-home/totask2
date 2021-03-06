package org.manathome.totask2.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** project does not exist. */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no such project")  // 404
public final class ProjectNotFoundException extends ToTaskExceptionBase {

    private static final long serialVersionUID = 1L;

    /** ctor. */
    public ProjectNotFoundException() {
        super();
    }

    /** ctor. */
    public ProjectNotFoundException(String message) {
        super(message);
    }

}
