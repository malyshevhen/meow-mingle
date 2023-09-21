package ua.mevhen.constants

import static ua.mevhen.constraints.DomainConstraints.USERNAME_MAX_SIZE
import static ua.mevhen.constraints.DomainConstraints.USERNAME_MIN_SIZE

class ValidationMessages {
    public static final def EMAIL_MESSAGE = 'It is not looks like an email.'
    public static final def PASSWORD_MESSAGE = 'Weak password.'
    public static final def USERNAME_MESSAGE =
            "Username must be at least $USERNAME_MIN_SIZE and not greater then $USERNAME_MAX_SIZE characters long."
}
