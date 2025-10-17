package com.flownews.config.security

import com.flownews.api.user.domain.UserRepository
import com.flownews.api.user.infra.CustomOAuth2User
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

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
                it.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                it.requestMatchers("/login/**").permitAll()
                it.requestMatchers("/topics", "/topics/topk", "/topics/*").permitAll()
                it.requestMatchers("/events/*", "/events/feed").permitAll()
                it.requestMatchers("/notifications/push").permitAll()
                it.anyRequest().authenticated()
            }.oauth2Login {
                it.successHandler { _, response, authentication ->
                    val customUser = (authentication.principal as CustomOAuth2User)
                    val user = customUser.getUser()

                    val authToken = authentication as OAuth2AuthenticationToken

                    if (user.deletedAt != null) {
                        val target =
                            if (authToken.authorizedClientRegistrationId == "google-mobile") {
                                "sijeom://auth/callback?error=DELETED"
                            } else {
                                "$redirectUrl/auth/callback?error=DELETED"
                            }
                        response.sendRedirect(target)
                        return@successHandler
                    }

                    val token = jwtService.createToken(user.id.toString())
                    val target =
                        if (authToken.authorizedClientRegistrationId == "google-mobile") {
                            "sijeom://auth/callback?token=$token"
                        } else {
                            "$redirectUrl/auth/callback?token=$token"
                        }
                    response.sendRedirect(target)
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
