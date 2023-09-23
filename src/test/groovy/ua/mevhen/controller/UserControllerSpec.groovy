package ua.mevhen.controller

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import ua.mevhen.domain.dto.UserInfo
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
        def userId = 'user123'
        def newUsername = 'newUsername'
        def userInfo = new UserInfo(id: userId, username: newUsername)

        userService.updateUsername(userId, newUsername) >> userInfo

        when:
        def result = mockMvc.perform(put("/api/user/$userId")
            .param("username", newUsername)
            .contentType('application/json'))

        then:
        result.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath('$.id').value(userId))
            .andExpect(jsonPath('$.username').value(newUsername))
    }

    @WithMockUser
    def "test delete endpoint"() {
        given:
        def userId = 'user123'

        when:
        def result = mockMvc.perform(delete("/api/user/$userId"))

        then:
        result.andExpect(MockMvcResultMatchers.status().isNoContent())
    }
}
