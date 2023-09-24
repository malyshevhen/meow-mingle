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

@Document(collection = 'comment')
@Builder
@ToString
class Comment {

    @MongoId
    private ObjectId id

    @DocumentReference
    private User author

    private String content

    @CreatedDate
    private LocalDate created

    @LastModifiedDate
    private LocalDate updated

    boolean equals(o) {
        if (this.is(o)) return true
        if (o == null || getClass() != o.class) return false

        Comment comment = (Comment) o

        if (author != comment.author) return false
        if (created != comment.created) return false
        if (id != comment.id) return false

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
