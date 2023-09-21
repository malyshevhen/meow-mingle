package ua.mevhen.util

import org.springframework.validation.BindingResult
import ua.mevhen.exceptions.ParameterValidationException

class ValidationUtils {

    static void handleFieldsErrors(final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            def messageBuilder = new StringBuilder()
            def errors = bindingResult.getFieldErrors()
            errors.forEach(err ->
                messageBuilder
                    .append(err.getField())
                    .append(" - ")
                    .append(err.getDefaultMessage())
                    .append(";"))

            throw new ParameterValidationException(messageBuilder.toString());
        }
    }

}
