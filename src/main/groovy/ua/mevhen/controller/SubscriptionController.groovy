package ua.mevhen.controller

import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.constraints.NotBlank
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.domain.dto.Subscription
import ua.mevhen.service.UserService

import java.security.Principal

import static ua.mevhen.domain.dto.SubscriptionOperation.SUBSCRIBE
import static ua.mevhen.domain.dto.SubscriptionOperation.UNSUBSCRIBE

@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping('/api/user')
@Slf4j
class SubscriptionController {

    private final UserService userService
    private final RedisTemplate<String, Subscription> redisSubscriptionTemplate
    
    SubscriptionController(
        UserService userService,
        RedisTemplate<String, Subscription> redisSubscriptionTemplate
    ) {
        this.userService = userService
        this.redisSubscriptionTemplate = redisSubscriptionTemplate
    }

    @PostMapping('/subscribe/{userId}')
    void subscribe(
        Principal principal,
        @PathVariable('userId') @NotBlank String subId
    ) {
        def subscription = new Subscription(principal.getName(), subId, SUBSCRIBE)
        log.info("User ${ principal.name } request for subscription to $subId")

        redisSubscriptionTemplate.opsForList().leftPush('subscription-events', subscription)
        log.info("User ${ principal.name } subscribed to user $subId")
    }

    @PostMapping('/unsubscribe/{userId}')
    void unsubscribe(
        Principal principal,
        @PathVariable('userId') @NotBlank String subId
    ) {
        def subscription = new Subscription(principal.getName(), subId, UNSUBSCRIBE)
        log.info("User ${ principal.name } request to unsubscribe from user $subId")

        redisSubscriptionTemplate.opsForList().leftPush('subscription-events', subscription)
        log.info("User ${ principal.name } unsubscribed from user $subId")
    }
}
