package ua.mevhen.domain.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder
import jakarta.validation.constraints.NotNull
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.MongoId

import java.time.LocalDate

@Document(collection = "user")
@Builder
@ToString
@EqualsAndHashCode
class User {

    @MongoId
    private ObjectId id

    @NotNull
    private String username

    @NotNull
    private String email

    @NotNull
    private String password

    private String role

    @DocumentReference
    private Set<User> subscriptions = new HashSet<>()

    @DocumentReference
    private Set<User> subscribers = new HashSet<>()

    @CreatedDate
    private LocalDate created

    @LastModifiedDate
    private LocalDate updated

    @Transient
    def subscribe = { User sub ->
        sub.addSubscriber(this)
        this.subscriptions.add(sub)
    }

    @Transient
    private def addSubscriber = { User u ->
        this.subscribers.add(u)
    }

    @Transient
    def unsubscribe = { User sub ->
        sub.removeSubscriber(this)
        this.subscriptions.removeIf { it.id == sub.id }
    }

    @Transient
    private def removeSubscriber(User u) {
        this.subscribers.removeIf { it.id == u.id }
    }

}
