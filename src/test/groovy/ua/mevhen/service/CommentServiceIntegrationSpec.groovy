package ua.mevhen.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification
import ua.mevhen.domain.dto.CommentRequest
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.model.Post
import ua.mevhen.domain.model.User

import static org.testcontainers.utility.DockerImageName.parse

@SpringBootTest
@Testcontainers
class CommentServiceIntegrationSpec extends Specification {

    @Shared
    static final def MONGO = new MongoDBContainer(parse('mongo:6.0')).withExposedPorts(27017)

    static {
        MONGO.start()
    }

    @DynamicPropertySource
    static void setMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add('spring.data.mongodb.uri', MONGO::getReplicaSetUrl)
    }

    @Autowired
    CommentService commentService

    @Autowired
    PostService postService

    @Autowired
    UserService userService

    User user
    Post post

    def setup() {
        def userRegistration = new User('John Doe', 'john.doe@mail.com', 'StrongP@ssword!')

        user = userService.save(userRegistration)

        def postRequest = new PostRequest(content: "Test post...")

        post = postService.save(userRegistration.username, postRequest.content())
    }

    def "test comment operations"() {
        given:
        def content = 'Test comment.🤕'
        def commentRequest = new CommentRequest(content)

        when:
        def savedComment = commentService.save(user.username, post.id.toString(), commentRequest)
        def postWithComment = postService.findById(post.id.toString())

        then:
        savedComment.id() != null
        postWithComment.comments.size() == 1
        postWithComment.comments[0].content == content


        when:
        def contentForUpdate = "Updated test comment!"
        def requestForUpdate = new CommentRequest(contentForUpdate)
        def updatedComment = commentService.update(user.username, savedComment.id(), requestForUpdate)
        def updatedPost = postService.findById(post.id.toString())

        then:
        updatedComment.content() == contentForUpdate
        updatedPost.comments.size() == 1
        updatedPost.comments.first().content == contentForUpdate

        when:
        commentService.delete(user.username, updatedComment.id())
        updatedPost = postService.findById(post.id.toString())

        then:
        updatedPost.comments.size() == 0
    }

}
