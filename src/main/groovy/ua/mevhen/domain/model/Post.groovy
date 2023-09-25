package ua.mevhen.domain.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
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
class Post {

    @MongoId
    private ObjectId id

    @DocumentReference
    private User author

    private String content

    @DocumentReference(lazy = true)
    private Set<User> likes = new HashSet()

    @DocumentReference(lazy = true)
    private Set<Comment> comments = new HashSet<>()

    @CreatedDate
    private LocalDate created

    @LastModifiedDate
    private LocalDate updated

    void addLike(User user) {
        this.likes.add(user)
    }

    void removeLike(User user) {
        this.likes.removeIf { it.id == user.id }
    }

}
