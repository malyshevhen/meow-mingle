package ua.mevhen.service

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.model.Post
import ua.mevhen.domain.model.User
import ua.mevhen.exceptions.PermissionDeniedException
import ua.mevhen.exceptions.PostNotFoundException
import ua.mevhen.mapper.UserMapper

import static org.testcontainers.utility.DockerImageName.parse

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class PostServiceIntegrationSpec extends Specification {

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
    PostService postService

    @Autowired
    UserService userService

    @Shared
    def userInfos = []

    def setup() {
        userInfos = [
                new User('John Doe', 'john.doe@mail.com', 'StrongP@ssword!'),
                new User('Jane Doe', 'jane.doe@mail.com', 'StrongP@ssword!'),
                new User('Jack Black', 'jack.black@mail.com', 'StrongP@ssword!')
        ]
                .collect { userService.save(it) }
                .collect { it.map(UserMapper::toUserInfo) }
    }

    def cleanup() {
        userInfos.each { userService.deleteById(new ObjectId(it.id())) }
        userInfos = []
    }

    def "test save method"() {
        given:
        def username = 'John Doe'
        def postRequest = new PostRequest(content: 'Test content')

        when:
        def savedPost = postService.save(username, postRequest.content())

        then:
        savedPost.id != null
        savedPost.content == postRequest.content()
        savedPost.author.id != null
        savedPost.created != null

        cleanup:
        postService.delete(savedPost.id.toString(), username)
    }

    def "test update method with valid authorization"() {
        given:
        def username = 'John Doe'
        def postRequest = new PostRequest(content: 'New Post')
        def postResponse = postService.save(username, postRequest.content())


        when:
        def updatedPostResponse = postService.update(postResponse.id, postRequest.content(), username)

        then:
        updatedPostResponse.content == postRequest.content()
    }

    def "test update method with invalid post id"() {
        given:
        def postId = new ObjectId()
        def username = "John Doe"
        def postRequest = new PostRequest(content: "Updated content")

        when:
        postService.update(postId, postRequest.content(), username)

        then:
        thrown(PostNotFoundException)
    }

    def "test delete method with valid authorization"() {
        given:
        def username = 'John Doe'
        def postRequest = new PostRequest(content: 'New Post')
        def postResponse = postService.save(username, postRequest.content())

        when:
        postService.delete(postResponse.id.toString(), username)

        then:
        noExceptionThrown()

        when:
        postService.delete(postResponse.id.toString(), username)


        then:
        thrown PostNotFoundException
    }

    def "test delete method with invalid authorization"() {
        given:
        def username = 'John Doe'
        def postRequest = new PostRequest(content: 'New Post')
        def postResponse = postService.save(username, postRequest.content())

        when:
        postService.delete(postResponse.id.toString(), 'Not Author')

        then:
        thrown(PermissionDeniedException)
    }

    def "test like and unlike operations"() {
        given:
        def publication = new PostRequest(content: 'test post')

        when:
        def userInfo = userInfos[0]
        def postResponse = postService.save(userInfo.username(), publication.content())

        then:
        userInfo.id() != null
        postResponse.id != null


        when:
        def postId = postResponse.id
        postService.addLike(userInfo.username(), postId.toString())
        def user = userService.findByUsername(userInfo.username())
        Post post = postService.findById(postId.toString())

        then:
        post.likes.contains(user)

        when:
        postService.removeLike(userInfo.username(), postId.toString())
        user = userService.findByUsername(userInfo.username())
        post = postService.findById(postId.toString())

        then:
        !post.likes.contains(user)
    }

    def "test updateComments method with valid post"() {
        given:
        def username = 'John Doe'
        def postRequest = new PostRequest(content: 'New Post')
        def postResponse = postService.save(username, postRequest.content())

        when:
        def post = postService.findById(postResponse.id.toString())
        post.content = 'Modified content'
        def updatedResponse = postService.update(post)

        then:
        updatedResponse.content == 'Modified content'

        cleanup:
        postService.delete(postResponse.id.toString(), username)
    }

    def "test updateComments method with invalid post"() {
        given:
        def username = 'John Doe'
        def postRequest = new PostRequest(content: 'New Post')
        def postResponse = postService.save(username, postRequest.content())

        when:
        def post = postService.findById(postResponse.id.toString())
        post.id = null
        post.content = 'Modified content'
        postService.update(post)

        then:
        thrown IllegalArgumentException

        cleanup:
        postService.delete(postResponse.id.toString(), username)
    }

}
