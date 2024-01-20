package ua.mevhen.controller

import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping('/{postId}')
    @ResponseStatus(HttpStatus.CREATED)
    void save(Principal principal, @PathVariable String postId, @RequestBody CommentRequest request) {
        def username = principal.name
        commentService.save(username, new ObjectId(postId), request)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping('/{postId}')
    def findByPostId(@PathVariable String postId, @RequestParam Integer size, @RequestParam Integer page) {
        def pageable = PageRequest.of(page, size)
        return commentService.getByPostId(new ObjectId(postId), pageable)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping('/{commentId}')
    void update(Principal principal, @PathVariable String commentId, @RequestBody CommentRequest request) {
        def username = principal.name
        commentService.update(username, new ObjectId(commentId), request)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping('/{commentId}')
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(Principal principal, @PathVariable String commentId) {
        def username = principal.name
        commentService.delete(username, new ObjectId(commentId))
    }

}
