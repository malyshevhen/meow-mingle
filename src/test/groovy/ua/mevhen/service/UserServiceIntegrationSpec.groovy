package ua.mevhen.service

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.exceptions.UserNotFoundException
import ua.mevhen.repository.UserRepository

class UserServiceIntegrationSpec extends AbstractIntegrationSpec {

    @Autowired
    UserService userService

    @Autowired
    UserRepository userRepository

    def "test save operations"() {
        given:
        def registration = new UserRegistration(
            username: 'testUser',
            email: 'test@example.com',
            password: 'password123'
        )

        when:
        def savedUserInfo = userService.save(registration)

        then:
        savedUserInfo.username == 'testUser'
        savedUserInfo.id != null
        !savedUserInfo.id.isBlank()

        when:
        def id = new ObjectId(savedUserInfo.id)
        def user = userRepository.findById(id).get()

        then:
        user.role == 'USER'
        user.created != null

        cleanup:
        userService.deleteById(savedUserInfo.id)
    }

    def "test updateUsername operation"() {
        given:
        def registration = new UserRegistration(
            username: 'testUser',
            email: 'test@example.com',
            password: 'password123'
        )

        def savedUserInfo = userService.save(registration)


        when:
        def id = savedUserInfo.id
        def newUsername = "newUsername"
        def updatedUserInfo = userService.updateUsername(id, newUsername)

        then:
        updatedUserInfo.username == newUsername

        cleanup:
        userService.deleteById(savedUserInfo.id)
    }

    def "test deleteById operation"() {
        given:
        def registration = new UserRegistration(
            username: 'testUser',
            email: 'test@example.com',
            password: 'password123'
        )

        def savedUserInfo = userService.save(registration)
        userService.deleteById(savedUserInfo.id)

        when:
        userService.findById(savedUserInfo.id)

        then:
        thrown UserNotFoundException
    }

    def "test subscribe and unsubscribe operations"() {
        given:
        def registration1 = new UserRegistration(
            username: 'testUser1',
            email: 'test@example.com',
            password: 'password123'
        )

        def registration2 = new UserRegistration(
            username: 'testUser2',
            email: 'test@example.com',
            password: 'password123'
        )

        when:
        def userInfo1 = userService.save(registration1)
        def userInfo2 = userService.save(registration2)

        then:
        userInfo1.id != null
        userInfo2.id != null

        when:
        userService.subscribe(userInfo1.username, userInfo2.id)
        def user1 = userService.findByUsername(userInfo1.username)
        def user2 = userService.findByUsername(userInfo2.username)

        then:
        user1.subscriptions.contains(user2)
        user2.subscribers.contains(user1)

        when:
        userService.unsubscribe(userInfo1.username, userInfo2.id)
        user1 = userService.findByUsername(userInfo1.username)
        user2 = userService.findByUsername(userInfo2.username)

        then:
        !user1.subscriptions.contains(user2)
        !user2.subscribers.contains(user1)
    }

}
