package ru.erudyt.online.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ru.erudyt.online.config.jwt.JwtAuthenticationEntryPoint
import ru.erudyt.online.config.jwt.JwtAuthorizationFilter

@Configuration
class SecurityConfig @Autowired constructor(
    private val jwtAuthorizationFilter: JwtAuthorizationFilter,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/v1/auth/anonym").permitAll()
            .antMatchers("/api/v1/auth/refresh").permitAll()
            .antMatchers("/api/**").authenticated()
            .anyRequest().permitAll()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}