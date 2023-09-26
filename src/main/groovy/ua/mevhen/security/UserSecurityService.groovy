package ua.mevhen.security

import groovy.util.logging.Slf4j
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ua.mevhen.repository.UserRepository

@Service
@Slf4j
class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository

    UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    @Override
    UserDetails loadUserByUsername(String username) {
        def user = userRepository.findByUsername(username)
            .map(SecureUser::new)
            .orElseThrow { -> new UsernameNotFoundException("Username: $username not found.") }

        log.info("Loaded UserDetails for username: $username")

        return user
    }

}
