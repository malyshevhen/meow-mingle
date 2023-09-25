package ua.mevhen.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import ua.mevhen.config.properties.SubscriptionServiceProperties
import ua.mevhen.domain.events.Subscription
import ua.mevhen.service.subscription.RedisSubscriptionWorker
import ua.mevhen.service.subscription.SubscriptionService

@Configuration
class SubscriptionServiceConfig {

    @Value('${subscription-task.thread-pool.thread-name-prefix}')
    private String threadNamePrefix

    @Value('${subscription-task.thread-pool.core-pool-size}')
    private Integer corePoolSize

    @Value('${subscription-task.thread-pool.max-pool-size}')
    private Integer maxPoolSize

    @Bean
    ThreadPoolTaskExecutor redisSubscriptionTaskExecutor() {
        def executor = new ThreadPoolTaskExecutor()

        executor.setCorePoolSize(this.corePoolSize)
        executor.setMaxPoolSize(this.maxPoolSize)
        executor.setThreadNamePrefix(this.threadNamePrefix)
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

