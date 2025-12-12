package com.flownews.api.user.api

import com.flownews.api.common.api.CurrentUser
import com.flownews.api.user.app.UserDeviceTokenUpdateRequest
import com.flownews.api.user.app.UserProfileUpdateRequest
import com.flownews.api.user.app.UserUpdateService
import com.flownews.api.user.app.UserWithdrawRequest
import com.flownews.api.user.domain.User
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserProfileUpdateApi(
    private val userUpdateService: UserUpdateService,
) {
    @PostMapping("/api/users/profile")
    fun updateProfile(
        @CurrentUser user: User,
        @RequestBody request: UserProfileUpdateRequest?,
    ) {
        userUpdateService.updateProfile(request, user.requireId())
    }

    @PostMapping("/api/users/device-token")
    fun updateDeviceToken(
        @CurrentUser user: User,
        @RequestBody request: UserDeviceTokenUpdateRequest,
    ) {
        val withUserId = request.withUserId(user.requireId())
        userUpdateService.updateDeviceToken(withUserId)
    }

    @PostMapping("/api/users/withdraw")
    fun withdraw(
        @CurrentUser user: User,
        @RequestBody request: UserWithdrawRequest,
    ) {
        userUpdateService.withdraw(user.requireId(), request)
    }
}
