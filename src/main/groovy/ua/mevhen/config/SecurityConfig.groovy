package ua.mevhen.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

import static org.springframework.web.cors.CorsConfiguration.ALL

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder()
    }

    @Bean
    CorsConfigurationSource corsFilter() {
        def config = new CorsConfiguration()
        config.addAllowedOrigin(ALL)
        config.addAllowedMethod(ALL)
        config.addAllowedHeader(ALL)
        config.allowCredentials = true

        def source = new UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)

        return source
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsFilter()) }
            .authorizeHttpRequests {
                it.requestMatchers('/v3/api-docs/**', '/swagger-ui/**', '/api/users/registration').permitAll()
                it.anyRequest().authenticated()
            }
            .httpBasic(Customizer.withDefaults())
            .build()
    }

}
