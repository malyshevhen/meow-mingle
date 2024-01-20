package ua.mevhen.domain.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import jakarta.validation.constraints.NotNull
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.data.mongodb.core.mapping.MongoId

import java.time.LocalDate

@Document(collection = 'user')
@ToString(includeFields = true,
    excludes = [
        'subscriptions',
        'subscribers'])
@EqualsAndHashCode(
    includeFields = true,
    excludes = [
        'subscriptions',
        'subscribers',
        'updated'
    ])
class User implements Mappable {

    @MongoId
    ObjectId id

    @NotNull
    String username

    @NotNull
    String email

    @NotNull
    String password

    String role

    @DocumentReference
    Set<User> subscriptions = new HashSet<>()

    @DocumentReference
    Set<User> subscribers = new HashSet<>()

    @CreatedDate
    LocalDate created

    @LastModifiedDate
    LocalDate updated

    @Version
    Long version

    User() {}

    User(String username, String email, String password) {
        this.username = username
        this.email = email
        this.password = password
    }

    void subscribe(User sub) {
        sub.addSubscriber(this)
        this.subscriptions.add(sub)
    }

    void addSubscriber(User u) {
        this.subscribers.add(u)
    }

    void unsubscribe(User sub) {
        sub.removeSubscriber(this)
        this.subscriptions.removeIf { it.id == sub.id }
    }

    void removeSubscriber(User u) {
        this.subscribers.removeIf { it.id == u.id }
    }
}
