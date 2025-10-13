package com.flownews.api.user.api

import com.flownews.api.common.api.CurrentUser
import com.flownews.api.user.app.UserDeviceTokenUpdateRequest
import com.flownews.api.user.app.UserProfileUpdateRequest
import com.flownews.api.user.app.UserUpdateService
import com.flownews.api.user.domain.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User Profile", description = "사용자 프로필 관리 API")
@RestController
@RequestMapping("/api/users")
class UserProfileUpdateApi(
    private val userUpdateService: UserUpdateService,
) {
    @Operation(summary = "사용자 프로필 완성", description = "생일과 성별을 입력하여 프로필을 완성합니다.")
    @PostMapping("/profile")
    fun updateProfile(
        @CurrentUser user: User,
        @RequestBody request: UserProfileUpdateRequest,
    ) {
        val withUserId = request.withUserId(user.requireId())
        userUpdateService.updateProfile(withUserId)
    }

    @Operation(summary = "사용자 FCM 토큰 업데이트", description = "사용자의 FCM 토큰을 업데이트합니다.")
    @PostMapping("/device-token")
    fun updateDeviceToken(
        @CurrentUser user: User,
        @RequestBody request: UserDeviceTokenUpdateRequest,
    ) {
        val withUserId = request.withUserId(user.requireId())
        userUpdateService.updateDeviceToken(withUserId)
    }
}
