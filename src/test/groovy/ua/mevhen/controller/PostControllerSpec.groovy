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
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.dto.PostResponse
import ua.mevhen.domain.model.Post
import ua.mevhen.exceptions.PermissionDeniedException
import ua.mevhen.exceptions.PostNotFoundException
import ua.mevhen.security.SecurityConfig
import ua.mevhen.service.PostService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@WebMvcTest(PostController)
@Import(SecurityConfig)
class PostControllerSpec extends Specification {

    static final POST_NOT_FOUND_EXCEPTION = new PostNotFoundException('PostNotFoundException')
    static final PERMISSION_DENIED_EXCEPTION = new PermissionDeniedException('PermissionDeniedException')
    static final ILLEGAL_ARGUMENT_EXCEPTION = new IllegalArgumentException('IllegalArgumentException')

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    PostService postService = Mock()

    @WithMockUser(username = 'John')
    def "test post endpoint and expect status 201"() {
        given:
        def content = 'Test Content'
        def postRequest = new PostRequest(content)

        postService.save('John', content) >> new Post(null, content)

        when:
        def response = performPost(postRequest)
        def responseContent = response.getContentAsString()
        def actualResponse = objectMapper.readValue(responseContent, PostResponse)

        then:
        response.status == 201
        actualResponse.content() == content
    }

    @WithMockUser(username = 'John')
    def "When trying to save new post, and #exception.message is thrown, expected status is: #httpStatus"(
            Exception exception,
            int httpStatus
    ) {
        given:
        def postRequest = new PostRequest(content: 'Test Content')

        when:
        postService.save('John', postRequest.content()) >> { throw exception }
        def response = performPost(postRequest)

        then:
        response.status == httpStatus

        where:
        exception                  | httpStatus
        ILLEGAL_ARGUMENT_EXCEPTION | 400
        POST_NOT_FOUND_EXCEPTION   | 404
    }

    @WithMockUser(username = 'John')
    def "test update endpoint and expect status 200"() {
        given:
        def content = 'Updated Content'
        def postRequest = new PostRequest(content: content)

        def postId = new ObjectId()

        postService.update(_, postRequest.content(), 'John') >> new Post(null, content)

        when:
        def response = performPut(postId.toString(), postRequest)
        def responseContent = response.getContentAsString()
        def actualResponse = objectMapper.readValue(responseContent, PostResponse)

        then:
        response.status == 200
        actualResponse.content() == content
    }

    @WithMockUser(username = 'John')
    def "When trying to update post, and #exception.message is thrown, expected status is: #httpStatus"(
            Exception exception,
            int httpStatus
    ) {
        given:
        def postId = new ObjectId()
        def postRequest = new PostRequest(content: 'Test Content')

        when:
        postService.update(postId, postRequest.content(), 'John') >> { throw exception }
        def response = performPut(postId.toString(), postRequest)

        then:
        response.status == httpStatus

        where:
        exception                   | httpStatus
        ILLEGAL_ARGUMENT_EXCEPTION  | 400
        PERMISSION_DENIED_EXCEPTION | 403
        POST_NOT_FOUND_EXCEPTION    | 404
    }

    @WithMockUser(username = 'John')
    def "test delete endpoint and expect status 204"() {
        given:
        def postId = new ObjectId().toString()

        when:
        postService.delete(postId, 'John')
        def response = performDelete(postId)

        then:
        response.status == 204
    }

    @WithMockUser(username = 'John')
    def "When trying to delete post, and #exception.message is thrown, expected status is: #httpStatus"(
            Exception exception,
            int httpStatus
    ) {
        given:
        def postId = new ObjectId()

        when:
        postService.delete(postId, 'John') >> { throw exception }
        def response = performDelete(postId.toString())


        then:
        response.status == httpStatus

        where:
        exception                   | httpStatus
        ILLEGAL_ARGUMENT_EXCEPTION  | 400
        PERMISSION_DENIED_EXCEPTION | 403
        POST_NOT_FOUND_EXCEPTION    | 404
    }

    private def performPost(PostRequest postRequest) {
        mockMvc.perform(
                post("/api/posts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andReturn()
                .response
    }

    private def performPut(String postId, PostRequest postRequest) {
        mockMvc.perform(
                put("/api/posts/$postId")
                        .contentType('application/json')
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andReturn()
                .response
    }

    private def performDelete(String postId) {
        mockMvc.perform(
                delete("/api/posts/$postId"))
                .andReturn()
                .response
    }

}
