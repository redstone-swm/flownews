package com.flownews.api.user.app

import com.flownews.api.user.domain.User
import com.flownews.api.user.domain.enums.Gender
import java.time.LocalDate

data class UserProfileUpdateResponse(
    val id: Long,
    val email: String,
    val name: String,
    val birthDate: LocalDate?,
    val gender: Gender?,
    val isProfileComplete: Boolean,
) {
    companion object {
        fun from(user: User): UserProfileUpdateResponse {
            return UserProfileUpdateResponse(
                id = user.id!!,
                email = user.email,
                name = user.name,
                birthDate = user.birthDate,
                gender = user.gender,
                isProfileComplete = user.isProfileComplete,
            )
        }
    }
}