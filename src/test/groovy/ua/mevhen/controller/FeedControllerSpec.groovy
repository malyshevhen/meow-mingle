package ua.mevhen.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import ua.mevhen.exceptions.UserNotFoundException
import ua.mevhen.service.FeedService

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class FeedControllerSpec extends Specification {

    private static final USER_NOT_FOUND_EXCEPTION
        = new UserNotFoundException('PostNotFoundException')

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    FeedService feedService = Mock(FeedService)

    @WithMockUser('John')
    def "Test ownerFeed endpoint"() {
        given:
        def size = 10
        def page = 0

        when:
        def result = performOwnerGet(size, page)

        then:
        result.response.status == 200
    }

    @WithMockUser('John')
    def "Test userFeed endpoint"() {
        given:
        def size = 10
        def page = 0

        when:
        def result = performUserGet('John', size, page)

        then:
        result.response.status == 200
    }

    @WithMockUser(username = 'John')
    def "When trying to get user's feed, and #exception.message is thrown, expected status is: #httpStatus"(
        Exception exception,
        int httpStatus
    ) {
        given:
        def size = 10
        def page = 0
        def pageable = PageRequest.of(page, size)

        when:
        feedService.getFeed('John', pageable) >> { throw exception }
        def result = performOwnerGet(size, page)

        then:
        result.response.status == httpStatus

        where:
        exception                      | httpStatus
        USER_NOT_FOUND_EXCEPTION       | 404
    }

    private def performOwnerGet(int size, int page) {
        mockMvc.perform(get("/api/feed")
            .param("size", size.toString())
            .param("page", page.toString())
            .accept(MediaType.APPLICATION_JSON)
        ).andReturn()
    }

    private def performUserGet(String username, int size, int page) {
        mockMvc.perform(get("/api/feed/$username")
            .param("size", size.toString())
            .param("page", page.toString())
            .accept(MediaType.APPLICATION_JSON)
        ).andReturn()
    }

}
