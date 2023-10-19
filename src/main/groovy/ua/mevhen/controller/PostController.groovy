package ua.mevhen.controller

import groovy.util.logging.Slf4j
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.api.PostsApi
import ua.mevhen.dto.CommentRequest
import ua.mevhen.dto.PageComments
import ua.mevhen.dto.PostRequest
import ua.mevhen.service.CommentService
import ua.mevhen.service.PostService

@Slf4j
@RestController
@RequestMapping('/api')
class PostController implements PostsApi {

    private final PostService postService
    private final CommentService commentService

    PostController(PostService postService, CommentService commentService) {
        this.postService = postService
        this.commentService = commentService
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Void> publishPost(PostRequest postRequest) {
        def username = SecurityContextHolder.context.authentication.name
        log.info("User '$username' is posting a new post")
        postService.save(username, postRequest)
        return ResponseEntity.status(201).build()
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Void> updatePost(String id, PostRequest request) {
        def username = SecurityContextHolder.context.authentication.name
        log.info("User '$username' is updating post with ID: $id")
        postService.update(id, request, username)
        return ResponseEntity.ok().build()
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Void> deletePost(String id) {
        def username = SecurityContextHolder.context.authentication.name
        log.info("User '$username' is deleting post with ID: $id")
        postService.delete(id, username)
        return ResponseEntity.status(204).build()
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Void> addComment(String postId, CommentRequest request) {
        def username = SecurityContextHolder.context.authentication.name
        log.info("Request to comment post with ID: $postId by user: $username")
        commentService.save(username, postId, request)
        return ResponseEntity.status(201).build()
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<PageComments> getPostComments(String postId, Integer size, Integer page) {
        log.info("Request to retrieve posts comments with PostID: $postId")
        def pageable = PageRequest.of(page, size)
        def postComments = commentService.getByPostId(postId, pageable)
        return new ResponseEntity(postComments, HttpStatus.OK)
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Void> updateComment(String commentId, CommentRequest request) {
        def username = SecurityContextHolder.context.authentication.name
        log.info("Request to update comment with ID: $commentId by user: $username")
        commentService.update(username, commentId, request)
        return ResponseEntity.status(200).build()
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<Void> deleteCommentById(String commentId) {
        def username = SecurityContextHolder.context.authentication.name
        log.info("Request to delete comment with ID: $commentId by user: $username")
        commentService.delete(username, commentId)
        return ResponseEntity.status(204).build()
    }
}
