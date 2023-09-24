package ua.mevhen.domain.model

import groovy.transform.ToString
import groovy.transform.builder.Builder
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.MongoId

import java.time.LocalDate

@Document(collection = 'post')
@Builder
@ToString
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

    boolean equals(o) {
        if (this.is(o)) return true
        if (o == null || getClass() != o.class) return false

        Post post = (Post) o

        if (author != post.author) return false
        if (created != post.created) return false
        if (id != post.id) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (author != null ? author.hashCode() : 0)
        result = 31 * result + (created != null ? created.hashCode() : 0)
        return result
    }

}
