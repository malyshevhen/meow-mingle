package ua.mevhen.domain.model

enum Role {
    USER('USER'),
    ADMIN('ADMIN')

    private final String value;

    Role(String value) {
        this.value = value
    }

}