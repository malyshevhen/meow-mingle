package ua.mevhen.controller

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.service.UserService

@RestController('/api/user/')
class SubscriptionController {

    private final UserService userService

    SubscriptionController(UserService userService) {
        this.userService = userService
    }

    @PostMapping('/subscribe/{id}')
    UserInfo subscribe(@PathVariable('id') subId) {
        return null
    }
}
