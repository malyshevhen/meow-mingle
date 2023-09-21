package ua.mevhen.domain.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalDate

@Document(collection = "user")
@ToString
@EqualsAndHashCode
class User {

    @Id
    String id

    @NotNull
    String username

    @NotNull
    String email

    @NotNull
    String password

    String status

    String bio

    @DBRef
    Set<User> subscribers = []

    @DBRef
    Set<User> subscriptions = []

    @CreatedDate
    LocalDate created

    @LastModifiedDate
    LocalDate updated
}
