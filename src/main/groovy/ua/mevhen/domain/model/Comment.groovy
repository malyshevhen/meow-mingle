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

}
