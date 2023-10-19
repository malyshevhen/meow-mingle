package ua.mevhen.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration

@Configuration
@SecurityScheme(
    type = SecuritySchemeType.HTTP,
    name = "basicAuth",
    scheme = "basic")
@OpenAPIDefinition(
    info = @Info(title = "Meow Mingle API", version = "0.0.1"),
    security = @SecurityRequirement(name = "basicAuth")
)
class SpringDocConfig {}
