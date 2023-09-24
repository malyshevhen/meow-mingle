package ua.mevhen.controller

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.validation.ConstraintViolationException
import org.bson.types.ObjectId
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import spock.lang.Specification
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.dto.PostResponse
import ua.mevhen.exceptions.PermissionDeniedException
import ua.mevhen.exceptions.PostNotFoundException
import ua.mevhen.service.PostService

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class PostControllerSpec extends Specification {

    public static final def POST_NOT_FOUND_EXCEPTION
        = new PostNotFoundException('PostNotFoundException')
    public static final def PERMISSION_DENIED_EXCEPTION
        = new PermissionDeniedException('PermissionDeniedException')
    public static final def CONSTRAINT_VIOLATION_EXCEPTION
        = new ConstraintViolationException('ConstraintViolationException', Set.of())

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
        def result = mockMvc.perform(
            post("/api/posts")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(postRequest))
        )

        then:
        result.andExpect(status().isCreated())
            .andExpect(jsonPath(/$.id/).value(postId))
            .andExpect(jsonPath(/$.content/).value("Test Content"))
    }

    @WithMockUser(username = 'John')
    def "When post is posted, and #exception.message is thrown expect status is: #httpStatus"(
        Exception exception,
        ResultMatcher resultMatcher,
        String httpStatus
    ) {
        given:
        def postRequest = new PostRequest(content: '')

        when:
        postService.save('John', postRequest) >> { throw exception }
        def result = mockMvc.perform(
            post("/api/posts")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(postRequest))
        )

        then:
        switch (exception) {
            case ConstraintViolationException:
                result.andExpect(resultMatcher)
                break
            case PostNotFoundException:
                result.andExpect(resultMatcher)
                break
            default:
                throw new AssertionError("Unexpected exception type: ${ exception.getClass().name }")
        }

        where:
        exception                      | resultMatcher           | httpStatus
        CONSTRAINT_VIOLATION_EXCEPTION | status().isBadRequest() | 'Bad request'
        POST_NOT_FOUND_EXCEPTION       | status().isNotFound()   | 'Not Found'
    }

    @WithMockUser(username = 'John')
    def "test update endpoint and expect status 200"() {
        given:
        def postRequest = new PostRequest(content: 'Updated Content')

        def postId = new ObjectId().toString()
        def postResponse = new PostResponse(id: postId, content: 'Updated Content')

        when:
        postService.update(postId, postRequest, 'John') >> postResponse
        def result = mockMvc.perform(
            put("/api/posts/$postId")
                .contentType('application/json')
                .content(objectMapper.writeValueAsString(postRequest))
        )

        then:
        result.andExpect(status().isOk())
            .andExpect(jsonPath(/$.id/).value(postId))
            .andExpect(jsonPath(/$.content/).value('Updated Content'))
    }

    @WithMockUser(username = 'John')
    def "When tri to update post, and #exception.message is thrown, expected status is: #httpStatus"(
        Exception exception,
        ResultMatcher resultMatcher,
        String httpStatus
    ) {
        given:
        def postId = new ObjectId().toString()
        def postRequest = new PostRequest(content: '')

        when:
        postService.update(postId, postRequest, 'John') >> { throw exception }
        def result = mockMvc.perform(
            put("/api/posts/$postId")
                .contentType('application/json')
                .content(objectMapper.writeValueAsString(postRequest))
        )

        then:
        switch (exception) {
            case ConstraintViolationException:
                result.andExpect(resultMatcher)
                break
            case PermissionDeniedException:
                result.andExpect(resultMatcher)
                break
            case PostNotFoundException:
                result.andExpect(resultMatcher)
                break
            default:
                throw new AssertionError("Unexpected exception type: ${ exception.getClass().name }")
        }

        where:
        exception                      | resultMatcher           | httpStatus
        CONSTRAINT_VIOLATION_EXCEPTION | status().isBadRequest() | 'Bad request'
        PERMISSION_DENIED_EXCEPTION    | status().isForbidden()  | 'Forbidden'
        POST_NOT_FOUND_EXCEPTION       | status().isNotFound()   | 'Not Found'
    }

    @WithMockUser(username = 'John')
    def "test delete endpoint and expect status 204"() {
        given:
        def postId = new ObjectId().toString()

        when:
        postService.delete(postId, 'John')
        def result = mockMvc.perform(
            delete("/api/posts/$postId"))

        then:
        result.andExpect(status().isNoContent())
    }

    @WithMockUser(username = 'John')
    def "When tri to delete post, and #exception.message is thrown, expected status is: #httpStatus"(
        Exception exception,
        ResultMatcher resultMatcher,
        String httpStatus
    ) {
        given:
        def postId = new ObjectId().toString()

        when:
        postService.delete(postId, 'John') >> { throw exception }
        def result = mockMvc.perform(
            delete("/api/posts/$postId"))

        then:
        switch (exception) {
            case ConstraintViolationException:
                result.andExpect(resultMatcher)
                break
            case PermissionDeniedException:
                result.andExpect(resultMatcher)
                break
            case PostNotFoundException:
                result.andExpect(resultMatcher)
                break
            default:
                throw new AssertionError("Unexpected exception type: ${ exception.getClass().name }")
        }

        where:
        exception                      | resultMatcher           | httpStatus
        CONSTRAINT_VIOLATION_EXCEPTION | status().isBadRequest() | 'Bad request'
        PERMISSION_DENIED_EXCEPTION    | status().isForbidden()  | 'Forbidden'
        POST_NOT_FOUND_EXCEPTION       | status().isNotFound()   | 'Not Found'
    }

}
