package ua.mevhen.constraints

class DomainConstraints {
    public static final String USER_PASSWORD_REGEXP = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$/
    public static final int USERNAME_MIN_SIZE = 3
    public static final int USERNAME_MAX_SIZE = 30
}
