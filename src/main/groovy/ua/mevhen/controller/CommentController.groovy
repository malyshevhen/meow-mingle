package ua.mevhen.controller

import groovy.util.logging.Slf4j
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.domain.dto.CommentRequest
import ua.mevhen.service.CommentService

import java.security.Principal

@Slf4j
@RestController
@RequestMapping('/api/posts/comment')
class CommentController {

    private final CommentService commentService

    CommentController(CommentService commentService) {
        this.commentService = commentService
    }

    @PostMapping('/{postId}')
    @ResponseStatus(HttpStatus.CREATED)
    void save(
        Principal principal,
        @PathVariable('postId') String postId,
        @RequestBody @Valid CommentRequest request
    ) {
        def username = principal.name
        log.info("Request to comment post with ID: $postId by user: $username")
        commentService.save(username,postId, request)
    }

    @PutMapping('/{commentId}')
    void update(
        Principal principal,
        @PathVariable('commentId') String commentId,
        @RequestBody @Valid CommentRequest request
    ) {
        def username = principal.name
        log.info("Request to update comment with ID: $commentId by user: $username")
        commentService.update(username, commentId, request)
    }

    @DeleteMapping('/{commentId}')
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
        Principal principal,
        @PathVariable('commentId') String commentId
    ) {
        def username = principal.name
        log.info("Request to delete comment with ID: $commentId by user: $username")
        commentService.delete(username, commentId)
    }

}
