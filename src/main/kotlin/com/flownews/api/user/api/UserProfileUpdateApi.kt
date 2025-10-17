package com.flownews.api.user.api

import com.flownews.api.common.api.CurrentUser
import com.flownews.api.user.app.UserDeviceTokenUpdateRequest
import com.flownews.api.user.app.UserProfileUpdateRequest
import com.flownews.api.user.app.UserUpdateService
import com.flownews.api.user.app.UserWithdrawRequest
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
        @RequestBody request: UserProfileUpdateRequest?,
    ) {
        userUpdateService.updateProfile(request, user.requireId())
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

    @Operation(summary = "사용자 탈퇴", description = "탈퇴 사유를 입력하여 탈퇴합니다.")
    @PostMapping("/withdraw")
    fun withdraw(
        @CurrentUser user: User,
        @RequestBody request: UserWithdrawRequest,
    ) {
        userUpdateService.withdraw(user.requireId(), request)
    }
}
