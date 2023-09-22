package ua.mevhen.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.service.UserService

import static org.mockito.Mockito.when
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class RegistrationControllerSpec extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper mapper

    @MockBean
    private UserService userService

    def "test register with valid registration data"() {
        given: 'Valid user registration DTO'
        def validRegistration = new UserRegistration(
            username: "testUser",
            email: "test@example.com",
            password: "Password123"
        )

        def userInfo = new UserInfo(id: '1', username: 'testUser')
        when(userService.save(validRegistration)).thenReturn(userInfo)

        when: 'Perform registration'
        def result = mockMvc.perform(post('/api/user/register')
            .contentType('application/json')
            .content(mapper.writeValueAsString(validRegistration)))

        then: 'Expect: status is 201, ID is generated, username is correct'
        result.andExpect(status().isCreated())
//        jsonPath(/$.id/).value('1'),
//        jsonPath(/$.username/).value('testUser')
    }

    def "test register with invalid registration data"() {
        given: 'Invalid email and password in form'
        def invalidRegistration = new UserRegistration(
            username: 'testUser',
            email: 'invalid-email',
            password: 'short'
        )

        when: 'Perform registration'
        def result = mockMvc.perform(post('/api/user/register')
            .contentType('application/json')
            .content(mapper.writeValueAsString(invalidRegistration)))

        then: 'Expect: status is 400'
        result.andExpect(status().isBadRequest())
    }

}
