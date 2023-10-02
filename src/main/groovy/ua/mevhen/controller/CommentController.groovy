package ua.mevhen.controller

import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ua.mevhen.domain.dto.CommentRequest
import ua.mevhen.domain.dto.CommentResponse
import ua.mevhen.service.CommentService

import java.security.Principal

import static ua.mevhen.constants.ValidationMessages.*
import static ua.mevhen.utils.Validator.*

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
        @ApiResponse(responseCode = '201', description = COMMENT_CREATED_MESSAGE),
        @ApiResponse(responseCode = '400', description = INVALID_COMMENT_REQUEST_MESSAGE),
        @ApiResponse(responseCode = '404', description = POST_NOT_FOUND_MESSAGE)
    ])
    @PostMapping('/{postId}')
    @ResponseStatus(HttpStatus.CREATED)
    void save(
        Principal principal,
        @PathVariable('postId') String postId,
        @RequestBody @Schema(description = "Comment request") CommentRequest request
    ) {
        validateCommentRequest(request, INVALID_COMMENT_REQUEST_MESSAGE)
        validateStringArg(postId, INVALID_POST_ID_MESSAGE)

        def username = principal.name
        log.info("Request to comment post with ID: $postId by user: $username")
        commentService.save(username, postId, request)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = 'Get comments by post ID')
    @ApiResponses(value = [
        @ApiResponse(responseCode = '200', description = COMMENT_RETRIEVED_MESSAGE),
        @ApiResponse(responseCode = '400', description = INVALID_COMMENT_RETRIEVE_MESSAGE),
        @ApiResponse(responseCode = '404', description = POST_NOT_FOUND_MESSAGE)
    ])
    @GetMapping('/{postId}')
    Page<CommentResponse> findByPostId(
        @PathVariable('postId') String postId,
        @RequestParam('size') Integer size,
        @RequestParam('page') Integer page
    ) {
        validatePageableArgs(size, page, INVALID_PAGEABLE_ARGS)
        validateStringArg(postId, INVALID_POST_ID_MESSAGE)

        log.info("Request to retrieve posts comments with PostID: $postId")

        def pageable = PageRequest.of(page, size)
        return commentService.getByPostId(postId, pageable)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = 'Update a comment')
    @ApiResponses(value = [
        @ApiResponse(responseCode = '200', description = COMMENT_UPDATED_MESSAGE),
        @ApiResponse(responseCode = '400', description = INVALID_COMMENT_REQUEST_MESSAGE),
        @ApiResponse(responseCode = '404', description = POST_NOT_FOUND_MESSAGE)
    ])
    @PutMapping('/{commentId}')
    void update(
        Principal principal,
        @PathVariable('commentId') String commentId,
        @RequestBody @Schema(description = 'Comment request') CommentRequest request
    ) {
        validateCommentRequest(request, INVALID_COMMENT_REQUEST_MESSAGE)
        validateStringArg(commentId, INVALID_COMMENT_ID_MESSAGE)

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
        @PathVariable('commentId') String commentId
    ) {
        validateStringArg(commentId, INVALID_COMMENT_ID_MESSAGE)

        def username = principal.name
        log.info("Request to delete comment with ID: $commentId by user: $username")

        commentService.delete(username, commentId)
    }

}
