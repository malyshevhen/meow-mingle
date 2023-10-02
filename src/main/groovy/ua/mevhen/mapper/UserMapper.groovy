package ua.mevhen.mapper

import org.springframework.stereotype.Component
import ua.mevhen.dto.UserInfo
import ua.mevhen.dto.UserRegistration
import ua.mevhen.domain.model.User

@Component
class UserMapper {

    User toUser(UserRegistration registration) {
        return new User(
            username: registration.username,
            email: registration.email,
            password: registration.password
        )
    }

    UserInfo toUserInfo(User user) {
        return new UserInfo(
            id: user.id.toString(),
            username: user.username
        )
    }
}