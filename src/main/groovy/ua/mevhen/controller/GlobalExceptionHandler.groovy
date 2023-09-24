package ua.mevhen.controller

import groovy.util.logging.Slf4j
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ua.mevhen.exceptions.PermissionDeniedException
import ua.mevhen.exceptions.PostNotFoundException
import ua.mevhen.exceptions.UserAlreadyExistsException
import ua.mevhen.exceptions.UserNotFoundException

@RestControllerAdvice
@Slf4j
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
        log.error("Bad request error: ${ ex.message }")
        return handleExceptionInternal(ex, ex.getMessage(),
            new HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler([
        UserNotFoundException,
        UsernameNotFoundException,
        PostNotFoundException
    ])
    protected final ResponseEntity handleNotFound(
        final RuntimeException ex,
        final WebRequest request
    ) {
        log.error("Not found error: ${ex.message}")
        return handleExceptionInternal(ex, ex.getMessage(),
            new HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler([PermissionDeniedException])
    protected final ResponseEntity handleForbidden(
        final RuntimeException ex,
        final WebRequest request
    ) {
        log.error("Forbidden error: ${ex.message}")
        return handleExceptionInternal(ex, ex.getMessage(),
            new HttpHeaders(), HttpStatus.FORBIDDEN, request)
    }
}
