package ua.mevhen.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification
import ua.mevhen.domain.dto.PostRequest
import ua.mevhen.domain.model.Post
import ua.mevhen.domain.model.User

import static org.testcontainers.utility.DockerImageName.parse

@SpringBootTest
@Testcontainers
class FeedServiceIntegrationSpec extends Specification {

    @Shared
    static final def MONGO = new MongoDBContainer(parse('mongo:6.0'))
            .withExposedPorts(27017)

    static {
        MONGO.start()
    }

    @DynamicPropertySource
    static void setMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add('spring.data.mongodb.uri', MONGO::getReplicaSetUrl)
    }

    @Autowired
    FeedService feedService

    @Autowired
    UserService userService

    @Autowired
    PostService postService

    def users = new ArrayList<User>()
    def posts = new ArrayList<Post>()

    def setup() {
        users = (1..2).collect {
            return userService.save(
                    new User(
                            username: "User$it",
                            email: "email$it@mail.com",
                            password: " Passw@rd$it"
                    )
            )
        }

        (1..5).each {
            users.each { userInfo ->
                def postRequest = new PostRequest(content: "test content: $it from ${userInfo.username}")
                posts.add(postService.save(userInfo.username, postRequest.content()))
            }
        }
    }

    def "test retrieving feed"() {
        given:
        def pageable = PageRequest.of(0, 10)

        when:
        userService.subscribe(users[0].username, users[1].id)
        def feed = feedService.getFeed(users[0].username, pageable)

        then:
        feed.totalElements == 5
        feed.totalPages == 1
    }

}
