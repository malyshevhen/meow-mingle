package ua.mevhen.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ua.mevhen.repository.UserRepository

@Service
class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository

    UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    @Override
    UserDetails loadUserByUsername(String username) {
        def user = userRepository.findByUsername(username)
            .orElseThrow { -> new UsernameNotFoundException("Username: $username not found.") }
        return new SecureUser(user: user)
    }

}
