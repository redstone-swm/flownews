package com.flownews.api.user.app

import com.flownews.api.user.domain.User

data class UserQueryResponse(
    val id: Long,
    val name: String,
    val email: String,
    val profileUrl: String?,
    val role: String,
    val isProfileComplete: Boolean,
) {
    companion object {
        fun from(user: User): UserQueryResponse =
            UserQueryResponse(
                id = user.requireId(),
                name = user.name,
                email = user.email,
                profileUrl = user.profileUrl,
                role = user.role.name,
                isProfileComplete = user.isProfileComplete,
            )
    }
}
