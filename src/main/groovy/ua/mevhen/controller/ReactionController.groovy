package ua.mevhen.controller

import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotNull
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.config.properties.ReactionServiceProperties
import ua.mevhen.domain.events.Reaction

import java.security.Principal

import static ua.mevhen.domain.events.ReactionOperation.LIKE
import static ua.mevhen.domain.events.ReactionOperation.UNLIKE

@Tag(name = 'ReactionController', description = 'Operations related to user reactions')
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
        summary = 'Add a like reaction to a post',
        description = 'Add a like reaction to a post for the authenticated user.',
        tags = ['ReactionController']
    )
    @PostMapping('/like/{postId}')
    void addReaction(
        Principal principal,
        @PathVariable('postId') @Parameter(description = "Post ID") @NotNull String postId
    ) {
        def reaction = new Reaction(principal.name, postId, LIKE)
        log.info("User: '${ principal.name }' request to like post: '$postId'.")
        redisReactionTemplate.opsForList().leftPush(properties.keyName, reaction)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
        summary = 'Remove a like reaction from a post',
        description = 'Remove a like reaction from a post for the authenticated user.',
        tags = ['ReactionController']
    )
    @DeleteMapping('/like/{postId}')
    void removeReaction(
        Principal principal,
        @PathVariable('postId') @Parameter(description = "Post ID") String postId
    ) {
        def reaction = new Reaction(principal.name, postId, UNLIKE)
        log.info("User: '${ principal.name }' request to remove like from post: '$postId'.")
        redisReactionTemplate.opsForList().leftPush(properties.keyName, reaction)
    }

}
