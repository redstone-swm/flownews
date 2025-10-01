package com.flownews.api.user.api

import com.flownews.api.user.app.UserProfileUpdateRequest
import com.flownews.api.user.app.UserUpdateService
import com.flownews.api.user.infra.CustomOAuth2User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User Profile", description = "사용자 프로필 관리 API")
@RestController
@RequestMapping("/api/users")
class UserProfileUpdateApi(
    private val userUpdateService: UserUpdateService,
    @Value("\${spring.security.oauth2.client.base-uri}") private val redirectUrl: String,
) {
    @Operation(summary = "사용자 프로필 완성", description = "생일과 성별을 입력하여 프로필을 완성합니다.")
    @PostMapping("/profile")
    fun updateProfile(
        @AuthenticationPrincipal principal: CustomOAuth2User,
        @RequestBody request: UserProfileUpdateRequest,
    ) {
        val updatedUser =
            userUpdateService.updateProfile(
                userId = principal.getUser().id!!,
                birthDate = request.birthDate,
                gender = request.gender,
            )
    }
}
