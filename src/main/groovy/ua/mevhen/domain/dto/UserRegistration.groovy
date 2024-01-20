package ua.mevhen.domain.dto

import ua.mevhen.domain.model.Mappable

record UserRegistration(String username, String email, String password)
        implements Mappable<UserRegistration> {}
