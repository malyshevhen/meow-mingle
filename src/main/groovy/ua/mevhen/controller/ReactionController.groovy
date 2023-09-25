package ua.mevhen.controller

import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.config.properties.ReactionServiceProperties
import ua.mevhen.domain.events.Reaction

import java.security.Principal

import static ua.mevhen.domain.events.ReactionOperation.LIKE
import static ua.mevhen.domain.events.ReactionOperation.UNLIKE

@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping('/api/reaction')
@Slf4j
class ReactionController {

    private final RedisTemplate<String, Reaction> redisReactionTemplate
    private final ReactionServiceProperties properties

    ReactionController(
        RedisTemplate<String, Reaction> redisReactionTemplate,
        ReactionServiceProperties properties
    ) {
        this.redisReactionTemplate = redisReactionTemplate
        this.properties = properties
    }

    @PostMapping('/like/{postId}')
    void addReaction(
        Principal principal,
        @PathVariable('postId') String postId
    ) {
        def reaction = new Reaction(principal.name, postId, LIKE)
        log.info("User: '${ principal.name }' request to like post: '$postId'.")
        redisReactionTemplate.opsForList().leftPush(properties.keyName, reaction)
    }

    @PostMapping('/unlike/{postId}')
    void removeReaction(
        Principal principal,
        @PathVariable('postId') String postId
    ) {
        def reaction = new Reaction(principal.name, postId, UNLIKE)
        log.info("User: '${ principal.name }' request to remove like from post: '$postId'.")
        redisReactionTemplate.opsForList().leftPush(properties.keyName, reaction)
    }

}
