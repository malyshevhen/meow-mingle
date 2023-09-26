package ua.mevhen.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.mongodb.config.EnableMongoAuditing

import java.time.OffsetDateTime

@Configuration
@EnableMongoAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
class MongoConfig {

    @Bean(name = "auditingDateTimeProvider")
    DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now())
    }

}