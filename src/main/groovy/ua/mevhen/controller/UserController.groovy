package ua.mevhen.controller

import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.service.UserService

@Tag(name = 'UserController', description = 'Operations related to user management')
@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping('/api/user')
@Slf4j
class UserController {

    private final UserService userService
    
    UserController(UserService userService) {
        this.userService = userService
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
        summary = 'Update user username',
        description = 'Update the username of a user by providing their user ID and a new username.',
        tags = ['UserController']
    )
    @PutMapping('/{id}')
    UserInfo updateUsername(
        @PathVariable('id') @Parameter(description = 'User ID') @NotBlank String userId,
        @RequestParam('username') @Parameter(description = 'Username') @NotBlank String username
    ) {
        def updatedUserInfo = userService.updateUsername(userId, username)
        log.info("User ID ${ userId } updated username to ${ username }")
        return updatedUserInfo
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
        summary = 'Delete user by ID',
        description = 'Delete a user by providing their user ID.',
        tags = ['UserController']
    )
    @DeleteMapping('/{id}')
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
        @PathVariable('id') @Parameter(description = 'User ID') @NotBlank String userId
    ) {
        userService.deleteById(userId)
        log.info("User ID ${ userId } deleted")
    }

}
