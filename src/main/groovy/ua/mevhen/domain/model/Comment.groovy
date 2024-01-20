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

@Document(collection = 'comment')
@ToString
@EqualsAndHashCode(
    includeFields = true,
    excludes = ['updated'])
class Comment implements Mappable<Comment>{

    @MongoId
    ObjectId id

    @DocumentReference(lazy = true)
    User author

    @DocumentReference
    Post post

    String content

    @CreatedDate
    LocalDate created

    @LastModifiedDate
    LocalDate updated
}
