package ua.mevhen.controller

import org.bson.types.ObjectId
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import ua.mevhen.service.AbstractIntegrationSpec
import ua.mevhen.service.UserService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
class SubscriptionControllerSpec extends AbstractIntegrationSpec {

    private static String USERNAME = 'John'

    @Autowired
    MockMvc mockMvc

    @SpringBean
    UserService userService = Mock(UserService)

    @WithMockUser(username = 'John')
    def "test subscribe to another user"() {
        given:
        def subId = new ObjectId().toString()

        when:
        def result = mockMvc.perform(post("/api/user/subscribe/$subId"))
        Thread.sleep(50)

        then:
        1 * userService.subscribe(USERNAME, subId)
        result.andExpect(status().isOk())
    }

    @WithMockUser(username = 'John')
    def "test unsubscribe from another user"() {
        given:
        def subId = new ObjectId().toString()

        when:
        def result = mockMvc.perform(post("/api/user/unsubscribe/$subId"))
        Thread.sleep(50)

        then:
        1 * userService.unsubscribe(USERNAME, subId)
        result.andExpect(status().isOk())
    }
}
