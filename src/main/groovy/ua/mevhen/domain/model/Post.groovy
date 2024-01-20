package ua.mevhen.domain.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.MongoId

import java.time.LocalDate

@Document(collection = 'post')
@ToString(
        includeFields = true,
        excludes = [
                'likes',
                'comments'
        ])
@EqualsAndHashCode(
        includeFields = true,
        excludes = [
                'likes',
                'comments',
                'updated'
        ])
class Post implements Mappable<Post> {

    @MongoId
    ObjectId id

    @DocumentReference
    User author

    String content

    @DocumentReference(lazy = true)
    Set<User> likes = new HashSet()

    @DocumentReference(lazy = true)
    Set<Comment> comments = new HashSet<>()

    @CreatedDate
    LocalDate created

    @LastModifiedDate
    LocalDate updated

    @Version
    Long version

    Post() {}

    Post(User author, String content) {
        this.author = author
        this.content = content
    }

    void addLike(User user) {
        this.likes.add(user)
    }

    void removeLike(User user) {
        this.likes.removeIf { it.id == user.id }
    }

    void addComment(Comment comment) {
        this.comments.add(comment)
    }

    void removeComment(Comment comment) {
        this.comments.removeIf { it.id == comment.id }
    }

    void updateComment(Comment comment) {
        removeComment(comment)
        addComment(comment)
    }

}
