package ua.mevhen.controller

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler([
        ConstraintViolationException,
        IllegalArgumentException
    ])
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    protected final ResponseEntity<Object> handleBadRequests(
        final RuntimeException ex,
        final WebRequest request
    ) {
        return handleExceptionInternal(ex, ex.getMessage(),
            new HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

}
