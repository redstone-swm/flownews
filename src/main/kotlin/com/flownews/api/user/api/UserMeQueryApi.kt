package com.flownews.api.user.api

import com.flownews.api.user.app.CustomOAuth2User
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SecurityRequirement(name = "bearerAuth")
@RestController
class UserMeQueryApi {
    @GetMapping("/users/me")
    fun getCurrentUser(
        @AuthenticationPrincipal principal: CustomOAuth2User,
    ): ResponseEntity<Any> {
        val user = principal.getUser()
        val response =
            mapOf(
                "id" to user.id,
                "name" to user.name,
                "email" to user.email,
                "profileUrl" to user.profileUrl,
                "role" to user.role,
            )
        return ResponseEntity.ok(response)
    }
}
