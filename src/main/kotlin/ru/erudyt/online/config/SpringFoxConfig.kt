package ru.erudyt.online.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.erudyt.online.config.jwt.JwtAuthorizationFilter
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SpringFoxConfig {

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .securityContexts(listOf(securityContext()))
            .securitySchemes(listOf(ApiKey("JWT", JwtAuthorizationFilter.AUTHORIZATION, "header")))
            .select()
            .apis(RequestHandlerSelectors.basePackage("ru.erudyt.online"))
            .paths(PathSelectors.any())
            .build()
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder().securityReferences(defaultAuth()).build()
    }

    private fun defaultAuth(): List<SecurityReference> {
        return listOf(
            SecurityReference(
                "JWT",
                arrayOf(AuthorizationScope("global", "accessEverything"))
            )
        )
    }
}