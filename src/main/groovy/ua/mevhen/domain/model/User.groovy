package ua.mevhen.domain.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.builder.Builder
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

import java.time.LocalDate

@Document(collection = "user")
@Builder
@ToString
@EqualsAndHashCode
class User {

    @Id
    private String id

    @NotNull
    private String username

    @NotNull
    private String email

    @NotNull
    private String password

    @DocumentReference
    private Set<User> subscribers = new HashSet<>()

    @DocumentReference
    private Set<User> subscriptions = new HashSet<>()

    @CreatedDate
    private LocalDate created

    @LastModifiedDate
    private LocalDate updated

    User(Map args) {
        this.username = args.username
        this.email = args.email
        this.password = args.password
    }

    def addSubscriber = { User u -> this.subscribers.add(u) }
    def subscribe = { User u ->
        {
            u.addSubscriber(this)
            this.subscriptions.add(u)
        }
    }
    def removeSubscriber = { User u -> this.subscribers.removeIf { it.id == u.id } }
    def unsubscribe = { User u ->
        {
            u.removeSubscriber(this)
            this.subscriptions.removeIf { it.id == u.id }
        }
    }

}
