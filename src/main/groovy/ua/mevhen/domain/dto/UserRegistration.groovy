package ua.mevhen.domain.dto

import static ua.mevhen.constraints.DomainConstraints.*

class UserRegistration {

    String username

    String email

    String password

    boolean validate() {
        if (this == null) return false
        if (this.username == null || this.email == null || this.password == null) return false
        if (this.username.length() <= USERNAME_MIN_SIZE || this.username.length() > USERNAME_MAX_SIZE) return false
        if (!this.email.matches(USER_EMAIL_REGEXP)) return false
        if (!this.password.matches(USER_PASSWORD_REGEXP)) return false
        return true
    }

}
