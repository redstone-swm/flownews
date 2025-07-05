package com.flownews.config.security

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
class SecurityConfig(private val jwtService: JwtService) {
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
                    val principal = authentication.principal
                    val oauthId = when (principal) {
                        is OAuth2User -> {
                            val provider =
                                (authentication as? OAuth2AuthenticationToken)?.authorizedClientRegistrationId
                                    ?: "unknown"
                            val providerId = principal.getAttribute<String>("sub") ?: principal.name
                            "${provider}_$providerId"
                        }

                        else -> principal.toString()
                    }
                    logger.info("로그인된 oauthId: $oauthId")
                    val token = jwtService.createTokenWithOauthId(oauthId)
                    val redirectUri = request.getParameter("redirect_uri") ?: "http://localhost:3000/oauth2/redirect"
                    response.sendRedirect("$redirectUri?token=$token")

                }
            }
        http.addFilterBefore(
            JwtAuthenticationFilter(jwtService),
            UsernamePasswordAuthenticationFilter::class.java
        )

        return http.build()
    }
}