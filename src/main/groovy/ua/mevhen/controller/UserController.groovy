package ua.mevhen.controller

import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.service.UserService

@RestController
@RequestMapping('/api/user')
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
        return userService.updateUsername(userId, username)
    }

    @DeleteMapping('/{id}')
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable('id') @NotBlank String userId) {
        userService.deleteById(userId)
    }

}
