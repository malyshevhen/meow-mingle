package ua.mevhen.service.subscription

import groovy.util.logging.Slf4j
import org.springframework.boot.CommandLineRunner
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
@Slf4j
class SubscriptionServiceRunner implements CommandLineRunner {

    private final RedisSubscriptionWorker redisSubscriptionWorker
    private final ThreadPoolTaskExecutor redisSubscriptionTaskExecutor

    SubscriptionServiceRunner(
        RedisSubscriptionWorker redisSubscriptionWorker,
        ThreadPoolTaskExecutor redisSubscriptionTaskExecutor
    ) {
        this.redisSubscriptionWorker = redisSubscriptionWorker
        this.redisSubscriptionTaskExecutor = redisSubscriptionTaskExecutor
    }

    @Override
    void run(String... args) throws Exception {
        log.info("Starting SubscriptionServiceRunner...")

        try {
            redisSubscriptionTaskExecutor.execute(redisSubscriptionWorker);
            log.info("Redis subscription worker has been executed successfully.");
        } catch (Exception e) {
            log.error("Error executing Redis subscription worker: ${e.message}");
        }

        log.info("SubscriptionServiceRunner has completed.");
    }
}
