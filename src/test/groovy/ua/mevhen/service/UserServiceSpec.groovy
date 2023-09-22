package ua.mevhen.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import spock.lang.Specification
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.domain.model.User
import ua.mevhen.mapper.UserMapper
import ua.mevhen.repository.UserRepository

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.when
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class UserServiceSpec extends Specification{

    @MockBean
    private UserRepository userRepository

    @Autowired
    private UserService userService

    private UserMapper mapper = new UserMapper()

    def "Save user for valid user form"() {
        given:
        def userForm = new UserRegistration(
            username: 'username',
            email: 'some@email.com',
            password: 'Valid@password1'
        )
        def user = mapper.toUser(userForm)

        when:
        when(userRepository.save(any(User))).thenReturn(user)
        def savedUserInfo = userService.save(userForm)

        then:
        savedUserInfo.username == userForm.username
    }

}
