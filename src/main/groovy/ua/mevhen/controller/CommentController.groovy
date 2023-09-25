package ua.mevhen.controller

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
        commentService.save(username,postId, request)
    }

    @PutMapping('/{postId}')
    void update(
        Principal principal,
        @PathVariable('postId') String postId,
        @RequestBody @Valid CommentRequest request
    ) {
        def username = principal.name
        commentService.update(username, postId, request)
    }

    @DeleteMapping('/{postId}/{commentId}')
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
        Principal principal,
        @PathVariable('postId') String postId,
        @PathVariable('commentId') String commentId
    ) {
        def username = principal.name
        commentService.delete(username, postId, commentId)
    }

}
