package ua.mevhen.controller

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ua.mevhen.exceptions.UserAlreadyExistsException
import ua.mevhen.exceptions.UserNotFoundException

@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler([
        ConstraintViolationException,
        IllegalArgumentException,
        UserAlreadyExistsException
    ])
    protected final ResponseEntity handleBadRequests(
        final RuntimeException ex,
        final WebRequest request
    ) {
        return handleExceptionInternal(ex, ex.getMessage(),
            new HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler([
        UserNotFoundException,
        UsernameNotFoundException
    ])
    protected final ResponseEntity handleNotFound(
        final RuntimeException ex,
        final WebRequest request
    ) {
        return handleExceptionInternal(ex, ex.getMessage(),
            new HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

}
