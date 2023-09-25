package ua.mevhen.controller

import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.constraints.NotBlank
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.config.properties.SubscriptionServiceProperties
import ua.mevhen.domain.events.Subscription

import java.security.Principal

import static ua.mevhen.domain.events.SubscriptionOperation.SUBSCRIBE
import static ua.mevhen.domain.events.SubscriptionOperation.UNSUBSCRIBE

@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping('/api/user')
@Slf4j
class SubscriptionController {

    private final RedisTemplate<String, Subscription> redisSubscriptionTemplate
    private final SubscriptionServiceProperties properties
    
    SubscriptionController(
        RedisTemplate<String, Subscription> redisSubscriptionTemplate,
        SubscriptionServiceProperties properties
    ) {
        this.redisSubscriptionTemplate = redisSubscriptionTemplate
        this.properties = properties
    }

    @PostMapping('/subscribe/{userId}')
    void subscribe(
        Principal principal,
        @PathVariable('userId') @NotBlank String subId
    ) {
        def subscription = new Subscription(principal.getName(), subId, SUBSCRIBE)
        log.info("User ${ principal.name } request for subscription to $subId")
        redisSubscriptionTemplate.opsForList().leftPush(properties.keyName, subscription)
    }

    @PostMapping('/unsubscribe/{userId}')
    void unsubscribe(
        Principal principal,
        @PathVariable('userId') @NotBlank String subId
    ) {
        def subscription = new Subscription(principal.getName(), subId, UNSUBSCRIBE)
        log.info("User ${ principal.name } request to unsubscribe from user $subId")
        redisSubscriptionTemplate.opsForList().leftPush('subscription-events', subscription)
    }
}
