package ua.mevhen.domain.model

import org.bson.types.ObjectId
import spock.lang.Specification

class UserSpec extends Specification {

    def "test User class properties"() {
        given:
        def user = new User(
            username: "testUser",
            email: "test@example.com",
            password: "password123"
        )

        expect:
        user.username == "testUser"
        user.email == "test@example.com"
        user.password == "password123"
        user.subscriptions.isEmpty()
        user.subscribers.isEmpty()
    }

    def "test subscribe and unsubscribe methods"() {
        given:
        def user1 = new User(
            id: new ObjectId(),
            username: "user1",
            email: "user1@example.com",
            password: "password123"
        )
        def user2 = new User(
            id: new ObjectId(),
            username: "user2",
            email: "user2@example.com",
            password: "password456"
        )

        when:
        user1.subscribe(user2)

        then:
        user1.subscriptions == [user2.id.toString()] as HashSet
        user2.subscribers == [user1.id.toString()] as HashSet

        when:
        user1.unsubscribe(user2)

        then:
        user1.subscriptions.isEmpty()
        user2.subscribers.isEmpty()
    }

    def "test addSubscriber and removeSubscriber methods"() {
        given:
        def user1 = new User(
            id: new ObjectId(),
            username: "user1",
            email: "user1@example.com",
            password: "password123"
        )
        def user2 = new User(
            id: new ObjectId(),
            username: "user2",
            email: "user2@example.com",
            password: "password456"
        )

        when:
        user1.addSubscriber(user2)

        then:
        user1.subscribers == [user2.id.toString()] as HashSet

        when:
        user1.removeSubscriber(user2)

        then:
        user1.subscribers.isEmpty()
    }
}

