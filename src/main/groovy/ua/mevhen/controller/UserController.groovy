package ua.mevhen.controller

import groovy.util.logging.Slf4j
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.api.UsersApi
import ua.mevhen.config.properties.SubscriptionServiceProperties
import ua.mevhen.domain.events.Subscription
import ua.mevhen.dto.UserInfo
import ua.mevhen.dto.UserRegistration
import ua.mevhen.service.UserService

import static ua.mevhen.domain.events.SubscriptionOperation.SUBSCRIBE
import static ua.mevhen.domain.events.SubscriptionOperation.UNSUBSCRIBE

@Slf4j
@RestController
@RequestMapping('/api')
class UserController implements UsersApi {

    private final UserService userService
    private final RedisTemplate<String, Subscription> redisSubscriptionTemplate
    private final SubscriptionServiceProperties properties

    UserController(
        UserService userService,
        RedisTemplate<String, Subscription> redisSubscriptionTemplate,
        SubscriptionServiceProperties properties
    ) {
        this.userService = userService
        this.redisSubscriptionTemplate = redisSubscriptionTemplate
        this.properties = properties
    }

    @Override
    ResponseEntity<UserInfo> register(UserRegistration regForm) {
        log.info("Received a registration request for username: ${ regForm.getUsername() }")
        def userInfo = userService.save(regForm)
        log.info("User registered successfully with username: ${ regForm.getUsername() }")
        return new ResponseEntity(userInfo, HttpStatus.CREATED)
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<UserInfo> updateUsername(String username) {
        def authentication = SecurityContextHolder.context.authentication
        def usernameToUpdate = authentication.name
        def userInfo = userService.updateUsername(usernameToUpdate, username)
        log.info("User '$usernameToUpdate' update username to '$username'")
        return new ResponseEntity(userInfo, HttpStatus.OK)
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Void> deleteUser() {
        def username = SecurityContextHolder.context.authentication.name
        userService.deleteByUsername(username)
        log.info("User '$username' deleted")
        return ResponseEntity.status(204).build()
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Void> subscribe(String userId) {
        def username = SecurityContextHolder.context.authentication.name
        def subscription = new Subscription(username, userId, SUBSCRIBE)
        log.info("User '$username' request for subscription to '$userId'")
        redisSubscriptionTemplate.opsForList().leftPush(properties.keyName, subscription)
        return ResponseEntity.status(200).build()
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Void> unsubscribe(String userId) {
        def username = SecurityContextHolder.context.authentication.name
        def subscription = new Subscription(username, userId, UNSUBSCRIBE)
        log.info("User '$username' request to unsubscribe from user '$userId'")
        redisSubscriptionTemplate.opsForList().leftPush('subscription-events', subscription)
        return ResponseEntity.status(200).build()
    }
}
