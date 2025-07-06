package com.flownews.config.security

import com.flownews.api.user.app.CustomOAuth2User
import com.flownews.api.user.domain.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.slf4j.LoggerFactory

@Configuration
@EnableWebSecurity
class SecurityConfig(private val jwtService: JwtService, private val userRepository: UserRepository) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val logger = LoggerFactory.getLogger(SecurityConfig::class.java)
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .anyRequest().permitAll()
            }
            .oauth2Login {
                it.successHandler { request, response, authentication ->
                    val principal = authentication.principal as OAuth2User
                    val id = principal.name
                    val token = jwtService.createToken(id)
                    val redirectUrl = System.getenv("FRONTEND_URL")
                        ?: "https://sijeom.kr"
                    response.sendRedirect("$redirectUrl/oauth/callback?token=$token")
                }
            }
            .sessionManagement { it.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS) }
        http.addFilterBefore(
            JwtAuthenticationFilter(jwtService, userRepository),
            UsernamePasswordAuthenticationFilter::class.java
        )

        return http.build()
    }
}