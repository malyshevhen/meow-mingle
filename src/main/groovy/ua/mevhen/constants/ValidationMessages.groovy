package ua.mevhen.constants

import static ua.mevhen.constraints.DomainConstraints.USERNAME_MAX_SIZE
import static ua.mevhen.constraints.DomainConstraints.USERNAME_MIN_SIZE

class ValidationMessages {
    public static final def EMAIL_MESSAGE = 'It is not looks like an email.'
    public static final def PASSWORD_MESSAGE = 'Weak password.'
    public static final String USERNAME_MESSAGE = 'Username must be at least 3 and not greater then 30 characters long.'
}
