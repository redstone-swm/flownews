package com.flownews.api.user.app

data class UserDeviceTokenUpdateRequest(
    val userId: Long,
    val deviceToken: String,
) {
    fun withUserId(userId: Long) = this.copy(userId = userId)
}
