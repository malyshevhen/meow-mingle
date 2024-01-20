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
import ua.mevhen.service.PostService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest(ReactionController)
@Import([SecurityConfig])
class ReactionControllerSpec extends Specification {

    static final USERNAME = 'John'
    static final POST_ID = new ObjectId()

    @Autowired
    MockMvc mockMvc

    @SpringBean
    PostService postService = Mock()

    @WithMockUser(username = 'John')
    def "should add a like reaction"() {
        when:
        def response = mockMvc.perform(
                post("/api/reaction/like/{postId}", POST_ID.toString()))
                .andReturn()
                .response

        then:
        1 * postService.addLike(USERNAME, POST_ID)
        0 * postService.removeLike(_, _)
        response.status == 200
    }

    @WithMockUser(username = "John")
    def "should remove a like reaction"() {
        when:
        def response = mockMvc.perform(delete("/api/reaction/like/{postId}", POST_ID))
                .andReturn()
                .response

        then:
        1 * postService.removeLike(USERNAME, POST_ID)
        0 * postService.addLike(_, _)
        response.status == 200
    }

}
