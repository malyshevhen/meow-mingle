package ua.mevhen.constraints

class DomainConstraints {
    public static final USER_PASSWORD_REGEXP = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$/
    public static final USERNAME_MIN_SIZE = 3
    public static final USERNAME_MAX_SIZE = 30
}
