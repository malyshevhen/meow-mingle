package ua.mevhen.controller

import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.domain.dto.CommentRequest
import ua.mevhen.domain.dto.CommentResponse
import ua.mevhen.service.CommentService

import java.security.Principal

@Tag(name = 'CommentController', description = 'Operations related to comments on posts')
@Slf4j
@RestController
@RequestMapping('/api/posts/comment')
class CommentController {

    private final CommentService commentService

    CommentController(CommentService commentService) {
        this.commentService = commentService
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = 'Create a new comment for a post')
    @ApiResponses(value = [
        @ApiResponse(responseCode = '201', description = 'Comments created successfully'),
        @ApiResponse(responseCode = '400', description = 'Comment request not valid'),
        @ApiResponse(responseCode = '404', description = 'Post not found')
    ])
    @PostMapping('/{postId}')
    @ResponseStatus(HttpStatus.CREATED)
    void save(
        Principal principal,
        @PathVariable('postId') String postId,
        @RequestBody @Schema(description = "Comment request") @Valid CommentRequest request
    ) {
        def username = principal.name
        log.info("Request to comment post with ID: $postId by user: $username")
        commentService.save(username, postId, request)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = 'Get comments by post ID')
    @ApiResponses(value = [
        @ApiResponse(responseCode = '200', description = 'Comments retrieved successfully'),
        @ApiResponse(responseCode = '404', description = 'Post not found')
    ])
    @GetMapping('/{postId}')
    Page<CommentResponse> findByPostId(
        @PathVariable('postId') String postId,
        @RequestParam('size') @NotNull Integer size,
        @RequestParam('page') @NotNull Integer page
    ) {
        def pageable = PageRequest.of(page, size)
        return commentService.getByPostId(postId, pageable)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = 'Update a comment')
    @ApiResponses(value = [
        @ApiResponse(responseCode = '200', description = 'Comments updated successfully'),
        @ApiResponse(responseCode = '400', description = 'Comment request not valid'),
        @ApiResponse(responseCode = '404', description = 'Post not found')
    ])
    @PutMapping('/{commentId}')
    void update(
        Principal principal,
        @PathVariable('commentId') @NotNull String commentId,
        @RequestBody @Schema(description = 'Comment request') @Valid CommentRequest request
    ) {
        def username = principal.name
        log.info("Request to update comment with ID: $commentId by user: $username")
        commentService.update(username, commentId, request)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = 'Delete a comment')
    @DeleteMapping('/{commentId}')
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
        Principal principal,
        @PathVariable('commentId') @NotNull String commentId
    ) {
        def username = principal.name
        log.info("Request to delete comment with ID: $commentId by user: $username")
        commentService.delete(username, commentId)
    }

}
