package ua.mevhen.controller

import org.bson.types.ObjectId
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.service.UserService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class SubscriptionControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @SpringBean
    UserService userService = Mock(UserService)

    @WithMockUser(username = 'John')
    def "test subscribe to another user"() {
        given:
        def subId = new ObjectId().toString()
        def name = 'John'

        def subscriber = new UserInfo(id: new ObjectId().toString(), username: name)
        def subscription = new UserInfo(id: new ObjectId().toString(), username: "Jane")

        when:
        userService.subscribe(name, subId) >> [subscriber, subscription]
        def result = mockMvc.perform(post("/api/user/subscribe/$subId"))

        then:
        result.andExpect(status().isOk())
    }

    @WithMockUser(username = 'John')
    def "test unsubscribe from another user"() {
        given:
        def subId = new ObjectId().toString()
        def name = 'John'

        def subscriber = new UserInfo(id: new ObjectId().toString(), username: name)
        def subscription = new UserInfo(id: new ObjectId().toString(), username: "Jane")

        when:
        userService.unsubscribe(name, subId) >> [subscriber, subscription]
        def result = mockMvc.perform(post("/api/user/unsubscribe/$subId"))

        then:
        result.andExpect(status().isOk())
    }
}
