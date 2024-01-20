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
import ua.mevhen.domain.model.User
import ua.mevhen.mapper.UserMapper

import static org.testcontainers.utility.DockerImageName.parse

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class ParallelReactionsInvocationSpec extends Specification {

//    @Shared
//    static final def MONGO = new MongoDBContainer(parse('mongo:6.0')).withExposedPorts(27017)
//
//    static {
//        MONGO.start()
//    }
//
//    @DynamicPropertySource
//    static void setMongoDbProperties(DynamicPropertyRegistry registry) {
//        registry.add('spring.data.mongodb.uri', MONGO::getReplicaSetUrl)
//    }
//
//    @Autowired
//    PostService postService
//
//    @Autowired
//    UserService userService
//
//    @Shared
//    def users = []
//
//    @Shared
//    def posts = []
//
//    def setup() {
//        users = [new User('John Doe', 'john.doe@mail.com', 'StrongP@ssword!'),
//                 new User('Jane Doe', 'jane.doe@mail.com', 'StrongP@ssword!'),
//                 new User('Jack Black', 'jack.black@mail.com', 'StrongP@ssword!')
//        ].collect { userService.save(it) }
//
//        posts = (1..10).collectMany { postIndex ->
//            users.collect { postService.save(it.username, "Post #$postIndex") }
//        }
//    }
//
//    def cleanup() {
//        users.each { userService.deleteById(it.id) }
//        users = []
//        posts.each {postService.delete(it.id, it.author.username)}
//    }
//
//    def "test"() {
//        expect:
//        users.size() != 0
//        posts.size() != 0
//    }
}
