package ua.mevhen.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ua.mevhen.domain.model.User

class SecureUser implements UserDetails{
    @Delegate User user

    SecureUser(User user) {
        this.user = user
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.roles.collect { new SimpleGrantedAuthority("ROLE_$it") }
    }

    @Override
    String getPassword() {
        return this.user.password
    }

    @Override
    String getUsername() {
        return this.user.username
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return true
    }
}
