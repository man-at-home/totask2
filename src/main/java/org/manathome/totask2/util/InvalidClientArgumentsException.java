package org.manathome.totask2.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/** task does not exist. */
@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED, reason = "not a valid call (arguments)")  // 404
public class InvalidClientArgumentsException extends ToTaskExceptionBase {

    private static final long serialVersionUID = 1L;

    public InvalidClientArgumentsException() {
    }

    public InvalidClientArgumentsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidClientArgumentsException(String message) {
        super(message);
    }

}
