package ua.mevhen.domain.model

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

    boolean equals(o) {
        if (this.is(o)) return true
        if (o == null || getClass() != o.class) return false

        User user = (User) o

        if (email != user.email) return false
        if (id != user.id) return false
        if (password != user.password) return false
        if (role != user.role) return false
        if (username != user.username) return false

        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (username != null ? username.hashCode() : 0)
        result = 31 * result + (email != null ? email.hashCode() : 0)
        result = 31 * result + (password != null ? password.hashCode() : 0)
        result = 31 * result + (role != null ? role.hashCode() : 0)
        return result
    }

}
