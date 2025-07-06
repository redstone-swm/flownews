package com.flownews.api.user.api

import com.flownews.api.user.app.CustomOAuth2User
import com.flownews.api.user.domain.UserRepository
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SecurityRequirement(name = "bearerAuth")
@RestController
class UserMeQueryApi @Autowired constructor(
    private val userRepository: UserRepository
) {
    @GetMapping("/users/me")
    fun me(@AuthenticationPrincipal principal: CustomOAuth2User?): ResponseEntity<Any> {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized")
        }
        val user = principal.getUser()
        val response = mapOf(
            "id" to user.id,
            "name" to user.name,
            "email" to user.email,
            "profileUrl" to user.profileUrl,
            "role" to user.role
        )
        return ResponseEntity.ok(response)
    }
}