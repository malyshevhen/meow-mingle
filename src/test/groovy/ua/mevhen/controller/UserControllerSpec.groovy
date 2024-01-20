package ua.mevhen.controller

import org.bson.types.ObjectId
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import ua.mevhen.domain.model.User
import ua.mevhen.security.SecurityConfig
import ua.mevhen.service.UserService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

@WebMvcTest(UserController)
@Import(SecurityConfig)
class UserControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @SpringBean
    UserService userService = Mock()

    @WithMockUser
    def "test updateUsername endpoint"() {
        given:
        def userId = new ObjectId()
        def newUsername = 'newUsername'
        def email = 'email@mail.com'
        def password = 'password'

        userService.updateUsername(userId, newUsername) >> new User(newUsername, email, password)

        when:
        def response = mockMvc.perform(put("/api/user/${userId.toString()}")
                .param("username", newUsername)
                .contentType('application/json'))
                .andReturn()
                .response

        then:
        response.status == 200
        response.contentAsString.contains(/"username":"$newUsername"/)
        response.contentAsString.contains(/"email":"$email"/)
        response.contentAsString.contains(/"password":"$password"/)
    }

    @WithMockUser
    def "test delete endpoint"() {
        given:
        def userId = new ObjectId()

        when:
        def response = mockMvc.perform(delete("/api/user/${userId.toString()}"))
                .andReturn()
                .response

        then:
        response.status == 204
    }
}
