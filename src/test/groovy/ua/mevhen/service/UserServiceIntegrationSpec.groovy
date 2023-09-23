package ua.mevhen.service


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.exceptions.UserNotFoundException

import static org.testcontainers.utility.DockerImageName.parse

@SpringBootTest
@Testcontainers
class UserServiceIntegrationSpec extends Specification {

    @Shared
    static def mongo = new MongoDBContainer(parse('mongo:6.0')).withExposedPorts(27017)

    static {
        mongo.start()
    }

    @DynamicPropertySource
    static void setMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add('spring.data.mongodb.uri', mongo::getReplicaSetUrl)
    }

    @Autowired
    UserService userService

    def "test save and findById operations"() {
        given:
        def registration = new UserRegistration(
            username: 'testUser',
            email: 'test@example.com',
            password: 'password123'
        )

        when:
        def savedUserInfo = userService.save(registration)
        def foundUserInfo = userService.findById(savedUserInfo.id)

        then:
        savedUserInfo.username == 'testUser'
        foundUserInfo.username == 'testUser'
    }

    def "test updateUsername operation"() {
        given:
        def registration = new UserRegistration(
            username: "testUser",
            email: "test@example.com",
            password: "password123"
        )

        def savedUserInfo = userService.save(registration)


        when:
        def id = savedUserInfo.id
        def newUsername = "newUsername"
        def updatedUserInfo = userService.updateUsername(id, newUsername)

        then:
        updatedUserInfo.username == newUsername
    }

    def "test deleteById operation"() {
        given:
        def registration = new UserRegistration(
            username: "testUser",
            email: "test@example.com",
            password: "password123"
        )

        def savedUserInfo = userService.save(registration)
        userService.deleteById(savedUserInfo.id)

        when:
        userService.findById(savedUserInfo.id)

        then:
        thrown UserNotFoundException
    }
}
