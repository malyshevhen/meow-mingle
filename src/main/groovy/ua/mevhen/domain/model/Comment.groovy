package ua.mevhen.domain.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalDate

@Document(collection = 'comment')
@Builder
@ToString
@EqualsAndHashCode
class Comment {

    @Id
    private String id

    @CreatedBy
    private User author

    private String content

    @CreatedDate
    private LocalDate created

    @LastModifiedDate
    private LocalDate updated
}
