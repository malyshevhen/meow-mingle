package ua.mevhen.controller

import groovy.util.logging.Slf4j
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.service.UserService

@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping('/api/user')
@Slf4j
class UserController {

    private final UserService userService
    
    UserController(UserService userService) {
        this.userService = userService
    }

    @PutMapping('/{id}')
    UserInfo updateUsername(
        @PathVariable('id') @NotBlank String userId,
        @RequestParam('username') @NotBlank String username
    ) {
        def updatedUserInfo = userService.updateUsername(userId, username)
        log.info("User ID ${ userId } updated username to ${ username }")
        return updatedUserInfo
    }

    @DeleteMapping('/{id}')
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable('id') @NotBlank String userId) {
        userService.deleteById(userId)
        log.info("User ID ${ userId } deleted")
    }

}
