package ua.mevhen.controller

import org.springframework.security.core.context.SecurityContextHolder

class AuthHelper {

    protected static def authenticatedUsername(def it = null) { SecurityContextHolder.context.authentication.name }

}


