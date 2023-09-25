package ua.mevhen.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import ua.mevhen.config.properties.ReactionServiceProperties
import ua.mevhen.domain.events.Reaction
import ua.mevhen.service.reaction.ReactionService
import ua.mevhen.service.reaction.RedisReactionWorker

@Configuration
class ReactionServiceConfig {

    @Value('${reaction-task.thread-pool.thread-name-prefix}')
    private String threadNamePrefix

    @Value('${reaction-task.thread-pool.core-pool-size}')
    private Integer corePoolSize

    @Value('${reaction-task.thread-pool.max-pool-size}')
    private Integer maxPoolSize

    @Bean
    ThreadPoolTaskExecutor redisReactionTaskExecutor() {
        def executor = new ThreadPoolTaskExecutor()

        executor.setCorePoolSize(this.corePoolSize)
        executor.setMaxPoolSize(this.maxPoolSize)
        executor.setThreadNamePrefix(this.threadNamePrefix)
        executor.initialize()

        return executor
    }

    @Bean
    RedisReactionWorker redisReactionWorker(
        RedisTemplate<String, Reaction> redisReactionTemplate,
        ReactionService reactionService,
        ReactionServiceProperties properties
    ) {
        return new RedisReactionWorker(redisReactionTemplate, reactionService, properties)
    }

}

