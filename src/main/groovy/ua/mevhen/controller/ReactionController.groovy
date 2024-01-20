package ua.mevhen.controller

import org.bson.types.ObjectId
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ua.mevhen.service.PostService

import java.security.Principal

@RestController
@RequestMapping('/api/reaction')
class ReactionController {
    final PostService postService

    ReactionController(PostService postService) {
        this.postService = postService
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping('/like/{postId}')
    void addReaction(Principal principal, @PathVariable String postId) {
        postService.addLike(principal.name, new ObjectId(postId))
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping('/like/{postId}')
    void removeReaction(Principal principal, @PathVariable String postId) {
        postService.removeLike(principal.name, new ObjectId(postId))
    }

}
