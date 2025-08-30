package com.flownews.api.user.api

import com.flownews.api.user.app.UserQueryResponse
import com.flownews.api.user.infra.CustomOAuth2User
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SecurityRequirement(name = "bearerAuth")
@RestController
class UserQueryApi {
    @GetMapping("/users/me")
    fun getCurrentUser(
        @AuthenticationPrincipal principal: CustomOAuth2User,
    ): ResponseEntity<UserQueryResponse> {
        val response = UserQueryResponse.from(principal.getUser())
        return ResponseEntity.ok(response)
    }
}
