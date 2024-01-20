package ua.mevhen.mapper

import org.bson.types.ObjectId
import spock.lang.Specification
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.domain.model.User

class UserMapperSpec extends Specification {

    def "test mapping UserRegistration to User"() {
        given:
        UserRegistration registration = new UserRegistration(
                username: 'testUser',
                email: 'test@example.com',
                password: 'password123'
        )

        when:
        User user = UserMapper.toUser(registration)

        then:
        user.username == registration.username
        user.email == registration.email
        user.password == registration.password
        user.subscribers.isEmpty()
        user.subscriptions.isEmpty()
    }

    def "test mapping User to UserInfo"() {
        given:
        def user = new User(
                id: new ObjectId(),
                username: 'testUser',
                email: 'test@example.com',
                password: 'password123'
        )

        when:
        UserInfo userInfo = UserMapper.toUserInfo(user)

        then:
        userInfo.id() == user.id.toString()
        userInfo.username() == user.username
    }
}

