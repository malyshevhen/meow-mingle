package ua.mevhen.controller

import org.bson.types.ObjectId
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import ua.mevhen.security.SecurityConfig
import ua.mevhen.service.UserService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest(SubscriptionController)
@Import(SecurityConfig)
class SubscriptionControllerSpec extends Specification {

    private static USERNAME = 'John'
    public static final SUB_ID = new ObjectId()

    @Autowired
    MockMvc mockMvc

    @SpringBean
    UserService userService = Mock()

    @WithMockUser(username = 'John')
    def "test subscribe to another user"() {
        when:
        def response = mockMvc.perform(post("/api/user/subscribe/${SUB_ID}"))
                .andReturn()
                .response

        then:
        1 * userService.subscribe(USERNAME, SUB_ID)
        0 * userService.unsubscribe(_, _)
        response.status == 200
    }

    @WithMockUser(username = 'John')
    def "test unsubscribe from another user"() {
        when:
        def response = mockMvc.perform(delete("/api/user/subscribe/${SUB_ID}"))
                .andReturn()
                .response

        then:
        1 * userService.unsubscribe(USERNAME, SUB_ID)
        0 * userService.subscribe(_, _)
        response.status == 200
    }
}
