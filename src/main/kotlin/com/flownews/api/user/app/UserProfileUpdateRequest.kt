package com.flownews.api.user.app

import com.flownews.api.user.domain.enums.Gender
import java.time.LocalDate

data class UserProfileUpdateRequest(
    val birthDate: LocalDate?,
    val gender: Gender?,
    val userId: Long,
) {
    fun withUserId(userId: Long) = this.copy(userId = userId)
}
