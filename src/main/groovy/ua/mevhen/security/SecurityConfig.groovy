package ua.mevhen.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers('/api/user/register').permitAll()
                it.requestMatchers('/api/user/**').hasRole('USER')
                it.requestMatchers('/api/posts/**').hasRole('USER')
                it.anyRequest().authenticated()
            }
            .build()
    }
}
