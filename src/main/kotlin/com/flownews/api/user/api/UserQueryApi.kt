package com.flownews.api.user.api

import com.flownews.api.common.api.CurrentUser
import com.flownews.api.user.app.UserQueryResponse
import com.flownews.api.user.domain.User
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SecurityRequirement(name = "bearerAuth")
@RestController
class UserQueryApi {
    @GetMapping("/api/users/me")
    fun getCurrentUser(
        @CurrentUser user: User,
    ): ResponseEntity<UserQueryResponse> {
        val response = UserQueryResponse.from(user)
        return ResponseEntity.ok(response)
    }
}
