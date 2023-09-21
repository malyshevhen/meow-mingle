package ua.mevhen.domain.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

record UserRegistration(
        @NotNull @NotBlank String username,
        @NotNull @NotBlank String email,
        @NotNull @NotBlank @Pattern(regexp = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$/) String password,
        String bio
) {

}