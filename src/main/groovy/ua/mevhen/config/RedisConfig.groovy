package ua.mevhen.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import ua.mevhen.domain.events.Reaction
import ua.mevhen.domain.events.Subscription

@Configuration
class RedisConfig {

    @Bean
    RedisTemplate<String, Subscription> redisSubscriptionTemplate(RedisConnectionFactory connectionFactory) {
        return buildRedisTemplate(connectionFactory, Subscription)
    }

    @Bean
    RedisTemplate<String, Reaction> redisReactionTemplate(RedisConnectionFactory connectionFactory) {
        return buildRedisTemplate(connectionFactory, Reaction)
    }

    private static <V> RedisTemplate<String, V> buildRedisTemplate(
        final RedisConnectionFactory connectionFactory,
        Class<V> valueType
    ) {
        def redisTemplate = new RedisTemplate<String, V>()
        RedisSerializer<String> keySerializer = new StringRedisSerializer()
        redisTemplate.keySerializer = keySerializer

        switch (valueType) {
            case String -> redisTemplate.valueSerializer = keySerializer
            default -> redisTemplate.valueSerializer = new Jackson2JsonRedisSerializer<>(valueType)
        }
        redisTemplate.connectionFactory = connectionFactory

        return redisTemplate
    }

}
