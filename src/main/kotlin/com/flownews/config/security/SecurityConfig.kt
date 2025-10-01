package com.flownews.config.security

import com.flownews.api.user.domain.UserRepository
import com.flownews.api.user.infra.CustomOAuth2User
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.http.HttpStatus

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    @Value("\${spring.security.oauth2.client.base-uri}") private val redirectUrl: String,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/events/**").authenticated()
                it.anyRequest().permitAll()
            }.oauth2Login {
                it.successHandler { _, response, authentication ->
                    val customUser = authentication.principal as CustomOAuth2User
                    val user = customUser.getUser()
                    val token = jwtService.createToken(user.id.toString())
                    response.sendRedirect("$redirectUrl/auth/callback?token=$token")
                }
            }.exceptionHandling {
                it.authenticationEntryPoint { _, response, _ ->
                    response.status = HttpStatus.UNAUTHORIZED.value()
                    response.contentType = "application/json"
                    response.writer.write("""{"error":"Authentication required"}""")
                }
            }.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
        http.addFilterBefore(
            JwtAuthenticationFilter(jwtService, userRepository),
            UsernamePasswordAuthenticationFilter::class.java,
        )

        return http.build()
    }
}
