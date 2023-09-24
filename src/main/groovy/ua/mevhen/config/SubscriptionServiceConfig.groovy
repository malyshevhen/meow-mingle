package ua.mevhen.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import ua.mevhen.config.properties.SubscriptionServiceProperties
import ua.mevhen.domain.dto.Subscription
import ua.mevhen.service.subscription.RedisSubscriptionWorker
import ua.mevhen.service.subscription.SubscriptionService

@Configuration
class SubscriptionServiceConfig {

    @Bean
    ThreadPoolTaskExecutor redisSubscriptionTaskExecutor() {
        def executor = new ThreadPoolTaskExecutor()

        executor.setCorePoolSize(1)
        executor.setMaxPoolSize(1)
        executor.setThreadNamePrefix("subscription-worker-")
        executor.initialize()

        return executor
    }

    @Bean
    RedisSubscriptionWorker redisSubscriptionWorker(
        RedisTemplate<String, Subscription> redisSubscriptionTemplate,
        SubscriptionService subscriptionService,
        SubscriptionServiceProperties properties
    ) {
        return new RedisSubscriptionWorker(redisSubscriptionTemplate, subscriptionService, properties)
    }

}

