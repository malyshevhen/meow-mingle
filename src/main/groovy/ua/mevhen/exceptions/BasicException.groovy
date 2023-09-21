package ua.mevhen.exceptions

import org.springframework.http.HttpStatus

class BasicException extends RuntimeException {

    private final HttpStatus status

    BasicException(
        final String message,
        final HttpStatus httpStatus
    ) {
        super(message);
        this.status = httpStatus;
    }
}
