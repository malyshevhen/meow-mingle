package ua.mevhen.controller

import org.springframework.security.core.context.SecurityContextHolder

class AuthHelper {

    protected static def authenticatedUsername() {
        SecurityContextHolder.context.authentication.name
    }
}


