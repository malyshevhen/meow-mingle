package ua.mevhen.service.reaction

import groovy.util.logging.Slf4j
import org.springframework.data.redis.core.RedisTemplate
import ua.mevhen.config.properties.ReactionServiceProperties
import ua.mevhen.domain.events.Reaction

import java.time.Duration

@Slf4j
class RedisReactionWorker implements Runnable {

    private final RedisTemplate<String, Reaction> reactionRedisTemplate
    private final ReactionService reactionService
    private final ReactionServiceProperties properties

    RedisReactionWorker(
        RedisTemplate<String, Reaction> reactionRedisTemplate,
        ReactionService reactionService,
        ReactionServiceProperties properties
    ) {
        this.reactionRedisTemplate = reactionRedisTemplate
        this.reactionService = reactionService
        this.properties = properties
    }

    @Override
    void run() {
        while (true) {
            def reaction = reactionRedisTemplate.opsForList()
                .rightPop(properties.keyName, Duration.ofMillis(properties.timeout))

            if (reaction != null) {
                log.info("Received reaction event: '$reaction'")
                reactionService.manageReaction(reaction)
            }
        }
    }

}
