package ua.mevhen.domain.dto

import spock.lang.Specification

class UserRegistrationSpec extends Specification {

    def "valid UserRegistration object"() {
        given:
        def validUserRegistration = new UserRegistration(
            username: "ValidUser",
            email: "valid@example.com",
            password: "ValidPassword123"
        )

        expect:
        validUserRegistration.validate()

    }

    def "UserRegistration with blank username should have violations"() {
        given:
        def userRegistration = new UserRegistration(
            username: "",
            email: "valid@example.com",
            password: "ValidPassword123"
        )

        expect:
        !userRegistration.validate()
    }

    def "UserRegistration with invalid email should have violations"() {
        given:
        def userRegistration = new UserRegistration(
            username: "ValidUser",
            email: "invalid-email", // Invalid email format
            password: "ValidPassword123"
        )

        expect:
        !userRegistration.validate()
    }

    def "UserRegistration with blank password should have violations"() {
        given:
        def userRegistration = new UserRegistration(
            username: "ValidUser",
            email: "valid@example.com",
            password: ""
        )

        expect:
        !userRegistration.validate()
    }
}
