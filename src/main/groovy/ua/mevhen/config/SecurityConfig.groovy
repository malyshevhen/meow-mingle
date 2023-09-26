package ua.mevhen.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import ua.mevhen.security.UserSecurityService

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    private final UserSecurityService userSecurityService

    SecurityConfig(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new NoOpPasswordEncoder() // TODO: change in prod!!!
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(
                    '/v3/api-docs/**',
                    '/swagger-ui/**',
                    '/api/user/register').permitAll()
                it.anyRequest().authenticated()
            }
            .userDetailsService(userSecurityService)
            .httpBasic(Customizer.withDefaults())
            .build()
    }

}
