package ua.mevhen.controller

import groovy.util.logging.Slf4j
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.api.ReactionsApi
import ua.mevhen.config.properties.ReactionServiceProperties
import ua.mevhen.domain.events.Reaction

import static ua.mevhen.domain.events.ReactionOperation.LIKE
import static ua.mevhen.domain.events.ReactionOperation.UNLIKE

@Slf4j
@RestController
@RequestMapping('/api')
class ReactionController implements ReactionsApi {

    private final RedisTemplate<String, Reaction> redisReactionTemplate
    private final ReactionServiceProperties properties

    ReactionController(
        RedisTemplate<String, Reaction> redisReactionTemplate,
        ReactionServiceProperties properties
    ) {
        this.redisReactionTemplate = redisReactionTemplate
        this.properties = properties
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<Void> addLike(String postId) {
        def username = SecurityContextHolder.context.authentication.name
        def reaction = new Reaction(username, postId, LIKE)
        log.info("User: '$username' request to like post: '$postId'.")
        redisReactionTemplate.opsForList().leftPush(properties.keyName, reaction)
        return ResponseEntity.ok().build()
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<Void> removeLike(String postId) {
        def username = SecurityContextHolder.context.authentication.name
        def reaction = new Reaction(username, postId, UNLIKE)
        log.info("User: '$username' request to remove like from post: '$postId'.")
        redisReactionTemplate.opsForList().leftPush(properties.keyName, reaction)
        return ResponseEntity.ok().build()
    }
}
