package ua.mevhen.domain.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

import static ua.mevhen.constraints.DomainConstraints.USERNAME_MAX_SIZE
import static ua.mevhen.constraints.DomainConstraints.USERNAME_MIN_SIZE
import static ua.mevhen.constraints.DomainConstraints.USER_BIO_MAX_SIZE
import static ua.mevhen.constraints.DomainConstraints.USER_PASSWORD_REGEXP
import static ua.mevhen.constraints.DomainConstraints.USER_EMAIL_REGEXP

record UserRegistration(
        @NotNull @NotBlank @Size(min = USERNAME_MIN_SIZE, max = USERNAME_MAX_SIZE) String username,
        @NotNull @NotBlank @Pattern(regexp = USER_EMAIL_REGEXP) String email,
        @NotNull @NotBlank @Pattern(regexp = USER_PASSWORD_REGEXP) String password,
        @Size(max = USER_BIO_MAX_SIZE) String bio
) {

}