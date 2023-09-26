package ua.mevhen.controller

import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.dto.PostResponse
import ua.mevhen.service.PostService

import java.security.Principal

@Tag(name = "PostController", description = "Operations related to user posts")
@RestController
@RequestMapping('/api/posts')
@Slf4j
class PostController {

    private final PostService postService

    PostController(PostService postService) {
        this.postService = postService
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
        summary = "Create a new post",
        description = "Create a new post for the authenticated user.",
        tags = ["PostController"]
    )
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
        summary = "Update a post",
        description = "Update an existing post for the authenticated user.",
        tags = ["PostController"]
    )
    @PutMapping('/{id}')
    PostResponse update(
        Principal principal,
        @PathVariable('id') @Parameter(description = "Post ID") String id,
        @RequestBody @Valid PostRequest request
    ) {
        def username = principal.getName()
        log.info("User '$username' is updating post with ID: $id")
        return postService.update(id, request, username)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
        summary = "Delete a post",
        description = "Delete an existing post for the authenticated user.",
        tags = ["PostController"]
    )
    @DeleteMapping('/{id}')
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
        Principal principal,
        @PathVariable('id') @Parameter(description = "Post ID") @NotBlank String id
    ) {
        def username = principal.getName()
        log.info("User '$username' is deleting post with ID: $id")
        postService.delete(id, username)
    }

}
