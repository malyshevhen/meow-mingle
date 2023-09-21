package ua.mevhen.constraints

class DomainConstraints {
    public static final def USER_PASSWORD_REGEXP = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$/
    public static final def USER_EMAIL_REGEXP = /^[\w\.-]+@[\w\.-]+\.\w+$/
    public static final def USERNAME_MIN_SIZE = 3
    public static final def USERNAME_MAX_SIZE = 30
    public static final def USER_BIO_MAX_SIZE = 1000
}
