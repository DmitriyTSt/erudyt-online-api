package ru.erudyt.online.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.erudyt.online.config.jwt.JwtAuthorizationFilter

@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("EruditeOnline API")
                    .description("API for <a href='https://erudit-online.ru'>https://erudit-online.ru</a>")
                    .version("v1")
            )
            .components(
                Components().addSecuritySchemes(
                    "JWT",
                    SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .name(JwtAuthorizationFilter.AUTHORIZATION)
                        .`in`(SecurityScheme.In.HEADER)
                )
            )
            .security(listOf(SecurityRequirement().addList("JWT")))
    }
}