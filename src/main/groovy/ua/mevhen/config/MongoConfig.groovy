package ua.mevhen.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import ua.mevhen.repository.CommentRepository
import ua.mevhen.repository.PostRepository
import ua.mevhen.repository.UserRepository

import java.time.OffsetDateTime

@Profile("!test")
@Configuration
@EnableMongoAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
@EnableMongoRepositories(basePackageClasses = [
        CommentRepository, PostRepository, UserRepository
])
class MongoConfig {

    @Bean(name = "auditingDateTimeProvider")
    DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now())
    }

}