package ua.mevhen.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.mapper.UserMapper
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
    def register(@RequestBody UserRegistration regForm) {
        def user = regForm.map(UserMapper::toUser)
        return userService.save(user)
                .map(UserMapper::toUserInfo)
    }

}