package ua.mevhen.domain.dto

import jakarta.validation.Validation
import spock.lang.Specification

class UserRegistrationSpec extends Specification {

    def factory = Validation.buildDefaultValidatorFactory()
    def validator = factory.validator

    def "valid UserRegistration object"() {
        given:
        def validUserRegistration = new UserRegistration(
            username: "ValidUser",
            email: "valid@example.com",
            password: "ValidPassword123"
        )

        when:
        def violations = validator.validate(validUserRegistration)

        then:
        violations.isEmpty()
    }

    def "UserRegistration with blank username should have violations"() {
        given:
        def userRegistration = new UserRegistration(
            username: "",
            email: "valid@example.com",
            password: "ValidPassword123"
        )

        when:
        def violations = validator.validate(userRegistration)

        then:
        violations.find { it.propertyPath.toString() == "username" } != null
    }

    def "UserRegistration with invalid email should have violations"() {
        given:
        def userRegistration = new UserRegistration(
            username: "ValidUser",
            email: "invalid-email", // Invalid email format
            password: "ValidPassword123"
        )

        when:
        def violations = validator.validate(userRegistration)

        then:
        violations.find { it.propertyPath.toString() == "email" } != null
    }

    def "UserRegistration with blank password should have violations"() {
        given:
        def userRegistration = new UserRegistration(
            username: "ValidUser",
            email: "valid@example.com",
            password: ""
        )

        when:
        def violations = validator.validate(userRegistration)

        then:
        violations.find { it.propertyPath.toString() == "password" } != null
    }
}
