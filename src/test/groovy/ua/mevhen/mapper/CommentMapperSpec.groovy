package ua.mevhen.mapper

import org.bson.types.ObjectId
import spock.lang.Specification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ua.mevhen.dto.CommentRequest
import ua.mevhen.domain.model.Comment
import ua.mevhen.domain.model.User

import java.time.LocalDate
import java.time.Month

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class CommentMapperSpec extends Specification {

    @Autowired
    private CommentMapper commentMapper

    def "test toResponse method"() {
        given:
        def id = new ObjectId()
        def userId = new ObjectId()

        def comment = new Comment(
            id: id,
            author: new User(id: userId),
            content: "Test Comment",
            created: LocalDate.of(2022, Month.JANUARY, 1),
            updated: LocalDate.of(2022, Month.FEBRUARY, 1)
        )

        when:
        def response = commentMapper.toResponse(comment)

        then:
        response.id == id.toString()
        response.authorId == userId.toString()
        response.content == "Test Comment"
        response.created == LocalDate.of(2022, Month.JANUARY, 1)
        response.updated == LocalDate.of(2022, Month.FEBRUARY, 1)
    }

    def "test toComment method"() {
        given:
        def request = new CommentRequest(content: "Test Comment")

        when:
        def comment = commentMapper.toComment(request)

        then:
        comment.content == "Test Comment"
    }
}

