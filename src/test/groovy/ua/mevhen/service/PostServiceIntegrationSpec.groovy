package ua.mevhen.service

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Shared
import ua.mevhen.dto.PostRequest
import ua.mevhen.dto.UserInfo
import ua.mevhen.dto.UserRegistration
import ua.mevhen.domain.model.Post
import ua.mevhen.exceptions.PermissionDeniedException
import ua.mevhen.exceptions.PostNotFoundException

class PostServiceIntegrationSpec extends AbstractIntegrationSpec {

    @Autowired
    PostService postService

    @Autowired
    UserService userService

    @Shared
    List<UserInfo> userInfos

    def setup() {
        def users = [
            new UserRegistration(
                username: 'John Doe',
                email: 'john.doe@mail.com',
                password: 'StrongP@ssword!'),
            new UserRegistration(
                username: 'Jane Doe',
                email: 'jane.doe@mail.com',
                password: 'StrongP@ssword!'),
            new UserRegistration(
                username: 'Jack Black',
                email: 'jack.black@mail.com',
                password: 'StrongP@ssword!'),
        ]

        userInfos = users.collect { userService.save(it) }
    }

    def "test save method"() {
        given:
        def username = 'John Doe'
        def postRequest = new PostRequest(content: 'Test content')

        when:
        def savedPostResponse = postService.save(username, postRequest)

        then:
        savedPostResponse.id != null
        savedPostResponse.content == postRequest.content
        savedPostResponse.authorId != null
        savedPostResponse.created != null

        cleanup:
        postService.delete(savedPostResponse.id, username)
        userInfos.each { userService.deleteById(it.id) }
    }

    def "test update method with valid authorization"() {
        given:
        def username = 'John Doe'
        def postRequest = new PostRequest(content: 'New Post')
        def postResponse = postService.save(username, postRequest)


        when:
        def updatedPostResponse = postService.update(postResponse.id, postRequest, username)

        then:
        updatedPostResponse.content == postRequest.content

        cleanup:
        userInfos.each { userService.deleteById(it.id) }
    }

    def "test update method with invalid post id"() {
        given:
        def postId = new ObjectId().toString()
        def username = "John Doe"
        def postRequest = new PostRequest(content: "Updated content")

        when:
        postService.update(postId, postRequest, username)

        then:
        thrown(PostNotFoundException)

        cleanup:
        userInfos.each { userService.deleteById(it.id) }
    }

    def "test delete method with valid authorization"() {
        given:
        def username = 'John Doe'
        def postRequest = new PostRequest(content: 'New Post')
        def postResponse = postService.save(username, postRequest)

        when:
        postService.delete(postResponse.id, username)

        then:
        noExceptionThrown()

        when:
        postService.delete(postResponse.id, username)


        then:
        thrown PostNotFoundException

        cleanup:
        userInfos.each { userService.deleteById(it.id) }
    }

    def "test delete method with invalid authorization"() {
        given:
        def username = 'John Doe'
        def postRequest = new PostRequest(content: 'New Post')
        def postResponse = postService.save(username, postRequest)

        when:
        postService.delete(postResponse.id, 'Not Author')

        then:
        thrown(PermissionDeniedException)

        cleanup:
        userInfos.each { userService.deleteById(it.id) }
    }

    def "test like and unlike operations"() {
        given:
        def publication = new PostRequest(content: 'test post')

        when:
        def userInfo = userInfos[0]
        def postResponse = postService.save(userInfo.username, publication)

        then:
        userInfo.id != null
        postResponse.id != null


        when:
        def postId = postResponse.id
        postService.addLike(userInfo.username, postId)
        def user = userService.findById(userInfo.id)
        Post post = postService.findById(postId)

        then:
        post.likes.contains(user)

        when:
        postService.removeLike(userInfo.username, postId)
        user = userService.findById(userInfo.id)
        post = postService.findById(postId)

        then:
        !post.likes.contains(user)

        cleanup:
        userInfos.each { userService.deleteById(it.id) }
    }

    def "test updateComments method with valid post"() {
        given:
        def username = 'John Doe'
        def postRequest = new PostRequest(content: 'New Post')
        def postResponse = postService.save(username, postRequest)

        when:
        def post = postService.findById(postResponse.id)
        post.content = 'Modified content'
        def updatedResponse = postService.update(post)

        then:
        updatedResponse.content == 'Modified content'

        cleanup:
        postService.delete(postResponse.id, username)
        userInfos.each { userService.deleteById(it.id) }
    }

    def "test updateComments method with invalid post"() {
        given:
        def username = 'John Doe'
        def postRequest = new PostRequest(content: 'New Post')
        def postResponse = postService.save(username, postRequest)

        when:
        def post = postService.findById(postResponse.id)
        post.id = null
        post.content = 'Modified content'
        postService.update(post)

        then:
        thrown IllegalArgumentException

        cleanup:
        postService.delete(postResponse.id, username)
        userInfos.each { userService.deleteById(it.id) }
    }

}
