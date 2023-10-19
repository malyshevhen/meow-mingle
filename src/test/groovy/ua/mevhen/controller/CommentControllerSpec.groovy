package ua.mevhen.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import ua.mevhen.dto.CommentRequest
import ua.mevhen.exceptions.UserNotFoundException
import ua.mevhen.service.CommentService

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class CommentControllerSpec extends Specification {

    private static final USER_NOT_FOUND_EXCEPTION = new UserNotFoundException('PostNotFoundException')
    private static final COMMENTS_URL = "/api/posts/comments"

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    CommentService commentService = Mock(CommentService)


    @WithMockUser(username = 'John')
    def "test save method"() {
        given:
        def postId = new ObjectId().toString()
        def commentRequest = new CommentRequest(content: "Sample comment")

        when:
        def result = performPost(postId, commentRequest)

        then:
        result.response.status == 201
    }

    @WithMockUser(username = 'John')
    def "test update method"() {
        given:
        def commentId = new ObjectId().toString()
        def commentRequest = new CommentRequest(content: "Updated comment")

        when:
        def result = performPut(commentId, commentRequest)

        then:
        result.response.status == 200
    }

    @WithMockUser(username = 'John')
    def "test delete method"() {
        given:
        def commentId = new ObjectId().toString()

        when:
        def result = performDelete(commentId)

        then:
        result.response.status == 204
    }

    @WithMockUser(username = 'John')
    def "When trying to perform  new comment, and #exception.message is thrown, expected status is: #httpStatus"(
        Exception exception,
        int httpStatus
    ) {
        given:
        def postId = new ObjectId().toString()
        def commentRequest = new CommentRequest(content: "Sample comment")

        when:
        commentService.save('John', postId, commentRequest) >> { throw exception }
        def result = performPost(postId, commentRequest)

        then:
        result.response.status == httpStatus

        where:
        exception                | httpStatus
        USER_NOT_FOUND_EXCEPTION | 404
    }

    private def performPost(String postId, CommentRequest commentRequest) {
        mockMvc.perform(
            post(COMMENTS_URL)
                .param('postId', postId)
                .content(objectMapper.writeValueAsString(commentRequest))
                .contentType("application/json"))
            .andReturn()
    }

    private def performPut(String commentId, CommentRequest commentRequest) {
        mockMvc.perform(
            put(COMMENTS_URL)
                .param('commentId', commentId)
                .content(objectMapper.writeValueAsString(commentRequest))
                .contentType("application/json"))
            .andReturn()
    }

    private def performDelete(String commentId) {
        mockMvc.perform(delete(COMMENTS_URL).param('commentId', commentId))
            .andReturn()
    }

}
