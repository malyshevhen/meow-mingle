package ua.mevhen.security

import org.bson.types.ObjectId
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Specification
import ua.mevhen.domain.model.User
import ua.mevhen.repository.UserRepository

class UserSecurityServiceSpec extends Specification {

    UserSecurityService securityService

    UserRepository userRepository

    def setup() {
        userRepository = Mock()
        securityService = new UserSecurityService(userRepository)
    }

    def "test loadUserByUsername with existing username"() {
        given:
        def username = "testUser"
        def user = new User(
                id: new ObjectId(),
                username: username,
                password: "testPassword",
                role: "ROLE_USER"
        )
        userRepository.findByUsername(username) >> Optional.of(user)

        when:
        def userDetails = securityService.loadUserByUsername(username)

        then:
        userDetails.username == username
        userDetails.password == "testPassword"
        userDetails.authorities.size() == 1
        userDetails.authorities[0].authority == "ROLE_USER"
    }

    def "test loadUserByUsername with non-existing username"() {
        given:
        def username = "nonExistentUser"
        userRepository.findByUsername(username) >> Optional.empty()

        when:
        securityService.loadUserByUsername(username)

        then:
        def exception = thrown Exception
        exception instanceof UsernameNotFoundException
        exception.message == "Username: $username not found."
    }
}

