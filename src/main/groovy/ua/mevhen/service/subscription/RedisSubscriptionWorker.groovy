package ua.mevhen.service.subscription

import groovy.util.logging.Slf4j
import org.springframework.data.redis.core.RedisTemplate
import ua.mevhen.config.properties.SubscriptionServiceProperties
import ua.mevhen.domain.events.Subscription

import java.time.Duration

@Slf4j
class RedisSubscriptionWorker implements Runnable {

    private final RedisTemplate<String, Subscription> redisSubscriptionTemplate
    private final SubscriptionService subscriptionService
    private final SubscriptionServiceProperties properties

    RedisSubscriptionWorker(
        RedisTemplate<String, Subscription> redisSubscriptionTemplate,
        SubscriptionService subscriptionService,
        SubscriptionServiceProperties properties
    ) {
        this.redisSubscriptionTemplate = redisSubscriptionTemplate
        this.subscriptionService = subscriptionService
        this.properties = properties
    }


    @Override
    void run() {
        while (true) {
            def subscription = redisSubscriptionTemplate.opsForList()
                .rightPop(properties.keyName, Duration.ofMillis(properties.timeout))

            if (subscription != null) {
                log.info("Received subscription event: $subscription")
                subscriptionService.manageSubscriptions(subscription)
            }
        }
    }

}
