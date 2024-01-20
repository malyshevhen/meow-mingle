package ua.mevhen.controller

import org.bson.types.ObjectId
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ua.mevhen.service.UserService

import java.security.Principal

@RestController
@RequestMapping('/api/user')
class SubscriptionController {

    final UserService userService

    SubscriptionController(UserService userService) {
        this.userService = userService
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping('/subscribe/{userId}')
    void subscribe(Principal principal, @PathVariable('userId') String subId) {
        userService.subscribe(principal.getName(), new ObjectId(subId))
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping('/subscribe/{userId}')
    void unsubscribe(Principal principal, @PathVariable('userId') String subId) {
        userService.unsubscribe(principal.getName(), new ObjectId(subId))
    }
}
