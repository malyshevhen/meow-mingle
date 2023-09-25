package ua.mevhen.service.reaction

import groovy.util.logging.Slf4j
import org.springframework.boot.CommandLineRunner
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
@Slf4j
class ReactionServiceRunner implements CommandLineRunner {

    private final RedisReactionWorker reactionWorker
    private final ThreadPoolTaskExecutor redisReactionTaskExecutor

    ReactionServiceRunner(
        RedisReactionWorker redisReactionWorker,
        ThreadPoolTaskExecutor redisReactionTaskExecutor
    ) {
        this.reactionWorker = redisReactionWorker
        this.redisReactionTaskExecutor = redisReactionTaskExecutor
    }

    @Override
    void run(String... args) throws Exception {
        log.info("Initializing ReactionServiceRunner...")

        try {
            redisReactionTaskExecutor.execute(reactionWorker)
            log.info("Redis reaction worker has been executed successfully.");
        } catch (Exception e) {
            log.error("Error executing Redis reaction worker: ${ e.message }");

        }
        log.info("ReactionServiceRunner has completed.");
    }
}
