package ua.mevhen.domain.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

import static ua.mevhen.constants.ValidationMessages.*
import static ua.mevhen.constraints.DomainConstraints.*

class UserRegistration {

    @NotNull
    @NotBlank
    @Size(min = USERNAME_MIN_SIZE,
        max = USERNAME_MAX_SIZE,
        message = USERNAME_MESSAGE)
    String username

    @NotNull
    @NotBlank
    @Email(message = EMAIL_MESSAGE)
    String email

    @NotNull
    @NotBlank
    @Pattern(regexp = USER_PASSWORD_REGEXP,
        message = PASSWORD_MESSAGE)
    String password

}
