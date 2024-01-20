package ua.mevhen.domain.model

enum Role {
    USER('ROLE_USER'),
    ADMIN('ROLE_ADMIN')

    final String value;

    Role(String value) {
        this.value = value
    }

}
