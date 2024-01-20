package ua.mevhen.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import ua.mevhen.domain.dto.CommentRequest
import ua.mevhen.exceptions.UserNotFoundException
import ua.mevhen.security.SecurityConfig
import ua.mevhen.service.CommentService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@WebMvcTest(CommentController)
@Import(SecurityConfig)
class CommentControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    CommentService commentService = Mock()


    @WithMockUser(username = 'John')
    def "test save method"() {
        given:
        def postId = new ObjectId().toString()
        def commentRequest = new CommentRequest(content: "Sample comment")

        when:
        def response = performPost(postId, commentRequest)

        then:
        response.status == 201
    }

    @WithMockUser(username = 'John')
    def "test update method"() {
        given:
        def commentId = new ObjectId().toString()
        def commentRequest = new CommentRequest(content: "Updated comment")

        when:
        def response = performPut(commentId, commentRequest)

        then:
        response.status == 200
    }

    @WithMockUser(username = 'John')
    def "test delete method"() {
        given:
        def commentId = new ObjectId().toString()

        when:
        def response = performDelete(commentId)

        then:
        response.status == 204
    }

    @WithMockUser(username = 'John')
    def "When trying to perform  new comment, and exception is thrown, expected status is: 404"() {
        given:
        def postId = new ObjectId()
        def commentRequest = new CommentRequest(content: "Sample comment")

        when:
        commentService.save('John', postId, commentRequest) >> { throw new UserNotFoundException('PostNotFoundException') }
        def response = performPost(postId.toString(), commentRequest)

        then:
        response.status == 404
    }

    private def performPost(String postId, CommentRequest commentRequest) {
        mockMvc.perform(
                post("/api/posts/comment/{postId}", postId)
                        .content(objectMapper.writeValueAsString(commentRequest))
                        .contentType("application/json"))
                .andReturn()
                .response
    }

    private def performPut(String commentId, CommentRequest commentRequest) {
        mockMvc.perform(
                put("/api/posts/comment/$commentId")
                        .content(objectMapper.writeValueAsString(commentRequest))
                        .contentType("application/json"))
                .andReturn()
                .response
    }

    private def performDelete(String commentId) {
        mockMvc.perform(delete("/api/posts/comment/{commentId}", commentId))
                .andReturn()
                .response
    }

}
