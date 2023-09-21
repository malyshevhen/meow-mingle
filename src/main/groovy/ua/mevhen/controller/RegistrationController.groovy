package ua.mevhen.controller

import jakarta.validation.Valid
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.service.UserService

import static ua.mevhen.util.ValidationUtils.handleFieldsErrors

@RestController
@RequestMapping('/api/user/register')
class RegistrationController {

    private final UserService userService

    RegistrationController(UserService userService) {
        this.userService = userService
    }

    @PostMapping
    UserInfo register(
        @Valid @RequestBody UserRegistration regForm,
        BindingResult bindingResult
    ) {
        handleFieldsErrors(bindingResult)
        return userService.save(regForm)
    }
}
