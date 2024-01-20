package ua.mevhen.controller

import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.service.PostService

import java.security.Principal

@RestController
@RequestMapping('/api/posts')
class PostController {

    private final PostService postService

    PostController(PostService postService) {
        this.postService = postService
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    def post(Principal principal, @RequestBody PostRequest postRequest) {
        def username = principal.getName()
        return postService.save(username, postRequest.content())
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping('/{id}')
    def update(Principal principal, @PathVariable String id, @RequestBody PostRequest request) {
        def username = principal.getName()
        return postService.update(new ObjectId(id), request.content(), username)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping('/{id}')
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(Principal principal, @PathVariable String id) {
        def username = principal.getName()
        postService.delete(new ObjectId(id), username)
    }

}
