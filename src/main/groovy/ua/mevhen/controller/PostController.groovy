package ua.mevhen.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.dto.PostResponse
import ua.mevhen.service.PostService

import java.security.Principal

@RestController
@RequestMapping('/api/posts')
class PostController {

    private final PostService postService

    PostController(PostService postService) {
        this.postService = postService
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    PostResponse post(
        Principal principal,
        @RequestBody @Valid PostRequest postRequest
    ) {
        return postService.save(principal.getName(), postRequest)
    }

    @PutMapping('/{id}')
    PostResponse update(
        Principal principal,
        @PathVariable('id') String id,
        @RequestBody @Valid PostRequest request
    ) {
        def username = principal.getName()
        return postService.update(id, request, username)
    }

    @DeleteMapping('/{id}')
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
        Principal principal,
        @PathVariable('id') @NotBlank String id
    ) {
        def username = principal.getName()
        postService.delete(id, username)
    }

}
