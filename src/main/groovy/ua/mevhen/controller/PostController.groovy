package ua.mevhen.controller

import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.dto.PostResponse
import ua.mevhen.service.PostService

import java.security.Principal

@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping('/api/posts')
@Slf4j
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
        def username = principal.getName()
        log.info("User '$username' is posting a new post")
        return postService.save(username, postRequest)
    }

    @PutMapping('/{id}')
    PostResponse update(
        Principal principal,
        @PathVariable('id') String id,
        @RequestBody @Valid PostRequest request
    ) {
        def username = principal.getName()
        log.info("User '$username' is updating post with ID: $id")
        return postService.update(id, request, username)
    }

    @DeleteMapping('/{id}')
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
        Principal principal,
        @PathVariable('id') @NotBlank String id
    ) {
        def username = principal.getName()
        log.info("User '$username' is deleting post with ID: $id")
        postService.delete(id, username)
    }

}
