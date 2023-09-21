package ua.mevhen.mapper

import spock.lang.Specification
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.domain.model.User

class UserMapperSpec extends Specification {

    UserMapper userMapper = new UserMapper()

    def "test mapping UserRegistration to User"() {
        given:
        UserRegistration registration = new UserRegistration(
            username: "testUser",
            email: "test@example.com",
            password: "password123"
        )

        when:
        User user = userMapper.toUser(registration)

        then:
        user.username == registration.username
        user.email == registration.email
        user.password == registration.password
        user.subscribers.isEmpty()
        user.subscriptions.isEmpty()
    }

    def "test mapping User to UserInfo"() {
        given:
        User user = new User(
            id: "123",
            username: "testUser",
            email: "test@example.com",
            password: "password123"
        )

        when:
        UserInfo userInfo = userMapper.toUserInfo(user)

        then:
        userInfo.id == user.id
        userInfo.username == user.username
    }
}

