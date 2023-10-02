package ua.mevhen.controller

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import ua.mevhen.dto.UserInfo
import ua.mevhen.service.UserService

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class UserControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @SpringBean
    UserService userService = Mock(UserService)

    @WithMockUser
    def "test updateUsername endpoint"() {
        given:
        def usernameToUpdate = 'user123'
        def newUsername = 'newUsername'
        def userInfo = new UserInfo(id: usernameToUpdate, username: newUsername)

        userService.updateUsername(usernameToUpdate, newUsername) >> userInfo

        when:
        def result = mockMvc.perform(put("/api/users")
            .param('username', usernameToUpdate)
            .param("username", newUsername)
            .contentType('application/json'))
            .andReturn()

        then:
        result.response.status == 200
    }

    @WithMockUser
    def "test delete endpoint"() {
        given:
        def userId = 'user123'

        when:
        def result = mockMvc.perform(delete("/api/users")).andReturn()

        then:
        result.response.status == 204
    }
}
