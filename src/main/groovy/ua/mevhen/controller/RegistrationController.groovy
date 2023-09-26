package ua.mevhen.controller

import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.service.UserService

@Tag(name = 'RegistrationController', description = 'Operations related to user registration')
@RestController
@RequestMapping('/api/user/register')
@Slf4j
class RegistrationController {

    private final UserService userService

    RegistrationController(UserService userService) {
        this.userService = userService
    }

    @Operation(
        summary = 'Register a new user',
        description = 'Register a new user with the provided registration details.',
        tags = ['RegistrationController']
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserInfo register(@RequestBody UserRegistration regForm) {
        if (!regForm.validate()) throw new IllegalArgumentException("Registration details not valid.")
        log.info("Received a registration request for username: ${ regForm.getUsername() }")
        def userInfo = userService.save(regForm)
        log.info("User registered successfully with username: ${ regForm.getUsername() }")
        return userInfo
    }

}
