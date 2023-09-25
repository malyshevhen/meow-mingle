package ua.mevhen.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new NoOpPasswordEncoder() // TODO: change in prod!!!
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html").permitAll()
                it.requestMatchers('/api/user/register').permitAll()
                it.requestMatchers('/api/user/**', '/api/posts/**', '/api/reaction/**').hasRole('USER')
                it.anyRequest().authenticated()
            }
            .httpBasic(Customizer.withDefaults())
            .build()
    }

}
