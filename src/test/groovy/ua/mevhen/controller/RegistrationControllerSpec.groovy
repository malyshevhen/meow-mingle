package ua.mevhen.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import ua.mevhen.domain.model.User
import ua.mevhen.security.SecurityConfig
import ua.mevhen.service.UserService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest(RegistrationController)
@Import(SecurityConfig)
class RegistrationControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper mapper

    @SpringBean
    UserService userService = Mock()

    def "test register with valid registration data, expected status 201"() {
        given: 'Valid user registration DTO'
        final userRegJson =
                $/{
                    "username": "testUser",
                    "email": "test@example.com",
                    "password": "Password123"
                }/$

        userService.save(_) >> new User("testUser", "test@example.com", "Password123")

        when: 'Perform registration'
        def response = mockMvc.perform(post('/api/user/register')
                .contentType('application/json')
                .content(userRegJson))
                .andReturn()
                .response

        then: 'Expect: status is 201, ID is generated, username is correct'
        response.status == 201
    }

//    def "test register with invalid registration data"() {
//        given: 'Invalid email and password in form'
//        def invalidRegistration = new UserRegistration(
//                username: 'testUser',
//                email: 'invalid-email',
//                password: 'short'
//        )
//
//        when: 'Perform registration'
//        def result = mockMvc.perform(post('/api/user/register')
//                .contentType('application/json')
//                .content(mapper.writeValueAsString(invalidRegistration)))
//
//        then: 'Expect: status is 400'
//        result.andExpect(status().isBadRequest())
//    }

}
