package ua.mevhen.controller

import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.dto.PostResponse
import ua.mevhen.service.PostService

import java.security.Principal

import static ua.mevhen.constants.ValidationMessages.INVALID_POST_CONTENT_MESSAGE
import static ua.mevhen.constants.ValidationMessages.INVALID_POST_ID_MESSAGE
import static ua.mevhen.utils.Validator.validateStringArg

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
        @RequestBody PostRequest postRequest
    ) {
        validateStringArg(postRequest.content, INVALID_POST_CONTENT_MESSAGE)

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
        @RequestBody PostRequest request
    ) {
        validateStringArg(request.content, INVALID_POST_CONTENT_MESSAGE)
        validateStringArg(id, INVALID_POST_ID_MESSAGE)

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
        @PathVariable('id') @Parameter(description = "Post ID") String id
    ) {
        validateStringArg(id, INVALID_POST_ID_MESSAGE)

        def username = principal.getName()
        log.info("User '$username' is deleting post with ID: $id")

        postService.delete(id, username)
    }

}
