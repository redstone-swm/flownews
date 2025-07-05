package com.flownews.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import kotlin.toString

@Configuration
@EnableWebSecurity
class SecurityConfig(private val jwtProvider: JwtProvider) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .anyRequest().permitAll()
            }
            .oauth2Login {
                it.successHandler { request, response, authentication ->
                    val principal = authentication.principal
                    val oauthId = when (principal) {
                        is org.springframework.security.oauth2.core.user.OAuth2User -> {
                            val provider = authentication.authorities.firstOrNull()?.authority ?: "unknown"
                            val providerId = principal.getAttribute<String>("sub") ?: principal.name
                            "${provider}_$providerId"
                        }

                        else -> principal.toString()
                    }
                    val token = jwtProvider.createTokenWithOauthId(oauthId)
                    response.sendRedirect("http://localhost:3000/oauth2/redirect?token=$token")
                }
            }
        http.addFilterBefore(
            JwtAuthenticationFilter(jwtProvider),
            org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter::class.java
        )

        return http.build()
    }
}