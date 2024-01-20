package ua.mevhen.mapper

import org.bson.types.ObjectId
import spock.lang.Specification
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.model.Comment
import ua.mevhen.domain.model.Post
import ua.mevhen.domain.model.User

import java.time.LocalDate
import java.time.Month

class PostMapperSpec extends Specification {

    def "test toResponse method"() {
        given:
        def id = new ObjectId()

        def post = new Post(
                id: id,
                author: new User(id: id),
                content: "Test Post",
                likes: [new User(id: id)],
                comments: [new Comment(id: id, author: new User(id: id))],
                created: LocalDate.of(2022, Month.JANUARY, 1),
                updated: LocalDate.of(2022, Month.FEBRUARY, 1)
        )

        when:
        def response = PostMapper.toResponse(post)

        then:
        response.id() == id.toString()
        response.authorId() == id.toString()
        response.content() == "Test Post"
        response.likes().size() == 1
        response.likes().first().id() == id.toString()
        response.comments().size() == 1
        response.comments().first().id() == id.toString()
        response.created() == LocalDate.of(2022, Month.JANUARY, 1)
        response.updated() == LocalDate.of(2022, Month.FEBRUARY, 1)
    }

    def "test toPost method"() {
        given:
        def request = new PostRequest(content: "Test Post")

        when:
        def post = PostMapper.toPost(request.content())

        then:
        post.content == "Test Post"
    }
}

