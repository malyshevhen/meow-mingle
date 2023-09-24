package ua.mevhen.controller

import groovy.util.logging.Slf4j
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.service.UserService

@RestController
@RequestMapping('/api/user/register')
@Slf4j
class RegistrationController {

    private final UserService userService

    RegistrationController(UserService userService) {
        this.userService = userService
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserInfo register(@RequestBody @Valid UserRegistration regForm) {
        log.info("Received a registration request for username: ${ regForm.getUsername() }");
        def userInfo = userService.save(regForm)
        log.info("User registered successfully with username: ${ regForm.getUsername() }");
        return userInfo
    }

}
