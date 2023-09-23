package ua.mevhen.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.service.UserService

@RestController
@RequestMapping('/api/user/register')
class RegistrationController {

    private final UserService userService

    RegistrationController(UserService userService) {
        this.userService = userService
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserInfo register(@RequestBody @Valid UserRegistration regForm) {
        return userService.save(regForm)
    }

}
