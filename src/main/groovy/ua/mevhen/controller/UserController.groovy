package ua.mevhen.controller

import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ua.mevhen.service.UserService

@RestController
@RequestMapping('/api/user')
class UserController {

    private final UserService userService

    UserController(UserService userService) {
        this.userService = userService
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping('/{id}')
    def updateUsername(@PathVariable String id, @RequestParam String username) {
        return userService.updateUsername(new ObjectId(id), username)
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping('/{id}')
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable String id) {
        userService.deleteById(new ObjectId(id))
    }
}
