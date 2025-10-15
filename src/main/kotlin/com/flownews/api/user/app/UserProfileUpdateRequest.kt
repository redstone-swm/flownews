package com.flownews.api.user.app

import com.flownews.api.user.domain.enums.Gender
import java.time.LocalDate

data class UserProfileUpdateRequest(
    val birthDate: LocalDate?,
    val gender: Gender?,
) {
}
