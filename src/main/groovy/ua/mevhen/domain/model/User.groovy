package ua.mevhen.domain.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder
import jakarta.validation.constraints.NotNull
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
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

    private Set<ObjectId> subscribers = new HashSet<>()

    private Set<ObjectId> subscriptions = new HashSet<>()

    @CreatedDate
    private LocalDate created

    @LastModifiedDate
    private LocalDate updated

    User() {}

    User(Map args) {
        this.username = args.username
        this.email = args.email
        this.password = args.password
    }

    def addSubscriber = { User u -> this.subscribers.add(u.id) }
    def subscribe = { User u ->
        {
            u.addSubscriber(this)
            this.subscriptions.add(u.id)
        }
    }
    def removeSubscriber = { User u -> this.subscribers.removeIf { it == u.id } }
    def unsubscribe = { User u ->
        {
            u.removeSubscriber(this)
            this.subscriptions.removeIf { it == u.id }
        }
    }

}
