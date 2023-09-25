package ua.mevhen.domain.model

import org.bson.types.ObjectId
import spock.lang.Specification

class PostSpec extends Specification {

    def "create a new Post object with a valid author, content, and likes"() {
        given:
        def author = new User(id: new ObjectId(), username: "test_author")
        def content = "This is a test post"
        def likes = [
            new User(id: new ObjectId(), username: "user1"),
            new User(id: new ObjectId(), username: "user2")
        ]

        when:
        def post = new Post(
            author: author,
            content: content,
            likes: likes.toSet()
        )

        then:
        post.author == author
        post.content == content
        post.likes == likes.toSet()
    }

    def "add a like to the post"() {
        given:
        def post = new Post()
        def user = new User(id: new ObjectId(), username: "test_user")

        when:
        post.addLike(user)

        then:
        post.likes.size() == 1
        post.likes.contains(user)
    }

    def "remove a like from the post"() {
        given:
        def userToRemove = new User(id: new ObjectId(), username: "user_to_remove")
        def post = new Post(likes: [userToRemove].toSet())

        when:
        post.removeLike(userToRemove)

        then:
        post.likes.size() == 0
        !post.likes.contains(userToRemove)
    }

}
