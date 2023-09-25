package ua.mevhen.controller

import org.bson.types.ObjectId
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Retry
import ua.mevhen.domain.model.User
import ua.mevhen.service.AbstractIntegrationSpec
import ua.mevhen.service.UserService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
class SubscriptionControllerSpec extends AbstractIntegrationSpec {

    private static USERNAME = 'John'
    public static final SUB_ID = new ObjectId().toString()

    @Autowired
    MockMvc mockMvc

    @SpringBean
    UserService userService = Mock(UserService)

    @Retry(count = 2)
    @WithMockUser(username = 'John')
    def "test subscribe to another user"() {
        when:
        def result = mockMvc.perform(post("/api/user/subscribe/${ SUB_ID }"))
        userService.findByUsername(USERNAME) >> new User()
        Thread.sleep(100)

        then:
        1 * userService.subscribe(USERNAME, SUB_ID)
        0 * userService.unsubscribe(_, _)
        result.andExpect(status().isOk())
    }

    @Retry(count = 2)
    @WithMockUser(username = 'John')
    def "test unsubscribe from another user"() {
        when:
        def result = mockMvc.perform(post("/api/user/unsubscribe/${ SUB_ID }"))
        userService.findByUsername(USERNAME) >> new User()
        Thread.sleep(100)

        then:
        1 * userService.unsubscribe(USERNAME, SUB_ID)
        0 * userService.subscribe(_, _)
        result.andExpect(status().isOk())
    }
}
