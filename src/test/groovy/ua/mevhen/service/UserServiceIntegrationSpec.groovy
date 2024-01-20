package ua.mevhen.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification
import ua.mevhen.domain.model.User
import ua.mevhen.exceptions.UserAlreadyExistsException
import ua.mevhen.exceptions.UserNotFoundException
import ua.mevhen.repository.UserRepository

import static org.testcontainers.utility.DockerImageName.parse

@SpringBootTest
@Testcontainers
class UserServiceIntegrationSpec extends Specification {

    @Shared
    static final def MONGO = new MongoDBContainer(parse('mongo:6.0')).withExposedPorts(27017)

    static {
        MONGO.start()
    }

    @DynamicPropertySource
    static void setMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add('spring.data.mongodb.uri', MONGO::getReplicaSetUrl)
    }

    @Autowired
    UserService userService

    @Autowired
    UserRepository userRepository

    def "test save operations"() {
        given:
        def registration = new User(
                username: 'testUser',
                email: 'test@example.com',
                password: 'password123'
        )

        when:
        def savedUserInfo = userService.save(registration)

        then:
        savedUserInfo.username == 'testUser'
        savedUserInfo.id != null
        savedUserInfo.id != null

        when:
        def id = savedUserInfo.id
        def user = userRepository.findById(id).get()

        then:
        user.role == 'ROLE_USER'
        user.created != null

        when:
        userService.save(user)

        then:
        thrown UserAlreadyExistsException

        cleanup:
        userService.deleteById(savedUserInfo.id)
    }

    def "test updateUsername operation"() {
        given:
        def registration = new User(
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
        def registration = new User(
                username: 'testUser',
                email: 'test@example.com',
                password: 'password123'
        )

        def savedUserInfo = userService.save(registration)
        userService.deleteById(savedUserInfo.id)

        when:
        userService.findByUsername(savedUserInfo.username)

        then:
        thrown UserNotFoundException
    }

    def "test subscribe and unsubscribe operations"() {
        given:
        def registration1 = new User(
                username: 'testUser1',
                email: 'test@example.com',
                password: 'password123'
        )

        def registration2 = new User(
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

        cleanup:
        userService.deleteById(user1.id)
        userService.deleteById(user2.id)
    }

}
