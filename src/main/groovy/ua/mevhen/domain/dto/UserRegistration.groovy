package ua.mevhen.domain.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

import static ua.mevhen.constants.ValidationMessages.*
import static ua.mevhen.constraints.DomainConstraints.*

record UserRegistration(

        @NotNull
        @NotBlank
        @Size(min = USERNAME_MIN_SIZE,
              max = USERNAME_MAX_SIZE,
              message = USERNAME_MESSAGE)
        String username,

        @NotNull
        @NotBlank
        @Pattern(regexp = USER_EMAIL_REGEXP,
                 message = EMAIL_MESSAGE)
        String email,

        @NotNull
        @NotBlank
        @Pattern(regexp = USER_PASSWORD_REGEXP,
                 message = PASSWORD_MESSAGE)
        String password,

        @Size(max = USER_BIO_MAX_SIZE)
        String bio
) {

}
