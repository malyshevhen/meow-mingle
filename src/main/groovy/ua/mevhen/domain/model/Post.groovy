package ua.mevhen.domain.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.MongoId

import java.time.LocalDate

@Document(collection = 'post')
@Builder
@ToString
@EqualsAndHashCode
class Post {

    @MongoId
    private ObjectId id

    @CreatedBy
    private User author

    private String content

    @DocumentReference
    private Set<User> likes = new HashSet()

    @DocumentReference
    private Set<Comment> comments = new HashSet<>()

    @CreatedDate
    private LocalDate created

    @LastModifiedDate
    private LocalDate updated
}
