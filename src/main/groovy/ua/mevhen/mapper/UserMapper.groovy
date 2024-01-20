package ua.mevhen.mapper

import org.springframework.stereotype.Component
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.domain.model.User

abstract class UserMapper {

    static User toUser(UserRegistration registration) {
        def user = new User()
        user.username = registration.username()
        user.email = registration.email()
        user.password = registration.password()

        return user
    }

    static UserInfo toUserInfo(User user) {
        return new UserInfo(
            id: user.id.toString(),
            username: user.username
        )
    }
}