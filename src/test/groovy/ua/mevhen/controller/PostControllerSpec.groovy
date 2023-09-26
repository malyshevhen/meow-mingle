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
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.dto.PostResponse
import ua.mevhen.exceptions.PermissionDeniedException
import ua.mevhen.exceptions.PostNotFoundException
import ua.mevhen.service.PostService

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class PostControllerSpec extends Specification {

    private static final POST_NOT_FOUND_EXCEPTION
        = new PostNotFoundException('PostNotFoundException')
    private static final PERMISSION_DENIED_EXCEPTION
        = new PermissionDeniedException('PermissionDeniedException')
    private static final ILLEGAL_ARGUMENT_EXCEPTION
        = new IllegalArgumentException('IllegalArgumentException')

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    PostService postService = Mock(PostService)

    @WithMockUser(username = 'John')
    def "test post endpoint and expect status 201"() {
        given:
        def postRequest = new PostRequest(content: 'Test Content')

        def postId = new ObjectId().toString()
        def postResponse = new PostResponse(id: "$postId", content: 'Test Content')

        when:
        postService.save('John', postRequest) >> postResponse
        def result = performPost(postRequest)
        def responseContent = result.response.getContentAsString()
        def actualResponse = objectMapper.readValue(responseContent, PostResponse)

        then:
        result.response.status == 201
        actualResponse.id == postId
        actualResponse.content == 'Test Content'
    }

    @WithMockUser(username = 'John')
    def "When trying to save new post, and #exception.message is thrown, expected status is: #httpStatus"(
        Exception exception,
        int httpStatus
    ) {
        given:
        def postRequest = new PostRequest(content: 'Test Content')

        when:
        postService.save('John', postRequest) >> { throw exception }
        def result = performPost(postRequest)

        then:
        result.response.status == httpStatus

        where:
        exception                  | httpStatus
        ILLEGAL_ARGUMENT_EXCEPTION | 400
        POST_NOT_FOUND_EXCEPTION   | 404
    }

    @WithMockUser(username = 'John')
    def "test update endpoint and expect status 200"() {
        given:
        def postRequest = new PostRequest(content: 'Updated Content')

        def postId = new ObjectId().toString()
        def postResponse = new PostResponse(id: postId, content: 'Updated Content')

        when:
        postService.update(postId, postRequest, 'John') >> postResponse
        def result = performPut(postId, postRequest)
        def responseContent = result.response.getContentAsString()
        def actualResponse = objectMapper.readValue(responseContent, PostResponse)

        then:
        result.response.status == 200
        actualResponse.id == postId
        actualResponse.content == 'Updated Content'
    }

    @WithMockUser(username = 'John')
    def "When trying to update post, and #exception.message is thrown, expected status is: #httpStatus"(
        Exception exception,
        int httpStatus
    ) {
        given:
        def postId = new ObjectId().toString()
        def postRequest = new PostRequest(content: 'Test Content')

        when:
        postService.update(postId, postRequest, 'John') >> { throw exception }
        def result = performPut(postId, postRequest)

        then:
        result.response.status == httpStatus

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
        def result = performDelete(postId)

        then:
        result.response.status == 204
    }

    @WithMockUser(username = 'John')
    def "When trying to delete post, and #exception.message is thrown, expected status is: #httpStatus"(
        Exception exception,
        int httpStatus
    ) {
        given:
        def postId = new ObjectId().toString()

        when:
        postService.delete(postId, 'John') >> { throw exception }
        def result = performDelete(postId)


        then:
        result.response.status == httpStatus

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
    }

    private def performPut(String postId, PostRequest postRequest) {
        mockMvc.perform(
            put("/api/posts/$postId")
                .contentType('application/json')
                .content(objectMapper.writeValueAsString(postRequest)))
            .andReturn()
    }

    private def performDelete(String postId) {
        mockMvc.perform(
            delete("/api/posts/$postId"))
            .andReturn()
    }

}
