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
import ua.mevhen.service.PostService
import ua.mevhen.service.UserService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
class ReactionControllerSpec extends AbstractIntegrationSpec {

    private static final REACTIONS_LIKES_URL = "/api/reactions/like"
    private static final USERNAME = 'John'
    private static final POST_ID = new ObjectId().toString()

    @Autowired
    MockMvc mockMvc

    @SpringBean
    PostService postService = Mock(PostService)

    @SpringBean
    UserService userService = Mock(UserService)

    @Retry(count = 2)
    @WithMockUser(username = 'John')
    def "should add a like reaction"() {
        when:
        def result = mockMvc.perform(post(REACTIONS_LIKES_URL)
            .param('postId', POST_ID))
        userService.findByUsername(USERNAME) >> new User()
        Thread.sleep(100)

        then:
        1 * postService.addLike(USERNAME, POST_ID)
        0 * postService.removeLike(_, _)
        result.andExpect(status().isOk())
    }

    @Retry(count = 2)
    @WithMockUser(username = "John")
    def "should remove a like reaction"() {
        when:
        def result = mockMvc.perform(delete(REACTIONS_LIKES_URL)
            .param('postId', POST_ID))
        userService.findByUsername(USERNAME) >> new User()
        Thread.sleep(100)

        then:
        1 * postService.removeLike(USERNAME, POST_ID)
        0 * postService.addLike(_, _)
        result.andExpect(status().isOk())
    }

}
