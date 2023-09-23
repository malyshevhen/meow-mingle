package ua.mevhen.controller

import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.service.UserService

import java.security.Principal

@RestController
@RequestMapping('/api/user')
class SubscriptionController {

    private final UserService userService

    SubscriptionController(UserService userService) {
        this.userService = userService
    }

    @PostMapping('/subscribe/{id}')
    List<UserInfo> subscribe(
        Principal principal,
        @PathVariable('id') @NotBlank String subId
    ) {
        return userService.subscribe(principal.getName(), subId)
    }

    @PostMapping('/unsubscribe/{id}')
    List<UserInfo> unsubscribe(
        Principal principal,
        @PathVariable('id') @NotBlank String subId
    ) {
        return userService.unsubscribe(principal.getName(), subId)
    }
}
