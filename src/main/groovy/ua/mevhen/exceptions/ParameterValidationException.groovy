package ua.mevhen.exceptions

import org.springframework.http.HttpStatus

class ParameterValidationException extends BasicException {

    ParameterValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST)
    }
}
