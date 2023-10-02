package ua.mevhen.service

import org.springframework.beans.factory.annotation.Autowired
import ua.mevhen.dto.CommentRequest
import ua.mevhen.dto.PostRequest
import ua.mevhen.dto.PostResponse
import ua.mevhen.dto.UserInfo
import ua.mevhen.dto.UserRegistration

class CommentServiceIntegrationSpec extends AbstractIntegrationSpec {

    @Autowired
    CommentService commentService

    @Autowired
    PostService postService

    @Autowired
    UserService userService

    UserInfo user
    PostResponse post

    def setup() {
        def userRegistration =
            new UserRegistration(
                username: 'John Doe',
                email: 'john.doe@mail.com',
                password: 'StrongP@ssword!'
            )

        user = userService.save(userRegistration)

        def postRequest = new PostRequest(content: "Test post...")

        post = postService.save(userRegistration.username, postRequest)
    }

    def cleanup() {
        postService.delete(post.id, user.username)
        userService.deleteById(user.id)
    }

    def "test comment operations"() {
        given:
        def content = 'Test comment.🤕'
        def commentRequest = new CommentRequest()
        commentRequest.content(content)

        when:
        def savedComment = commentService.save(user.username, post.id, commentRequest)
        def postWithComment = postService.findById(post.id)

        then:
        savedComment.id != null
        postWithComment.comments.size() == 1
        postWithComment.comments[0].content == content


        when:
        def contentForUpdate = "Updated test comment!"
        def requestForUpdate = new CommentRequest()
        requestForUpdate.content = contentForUpdate
        def updatedComment = commentService.update(user.username, savedComment.id, requestForUpdate)
        def updatedPost = postService.findById(post.id)

        then:
        updatedComment.content == contentForUpdate
        updatedPost.comments.size() == 1
        updatedPost.comments[0].content == contentForUpdate

        when:
        commentService.delete(user.username, updatedComment.id)
        updatedPost = postService.findById(post.id)

        then:
        updatedPost.comments.size() == 0
    }

}
