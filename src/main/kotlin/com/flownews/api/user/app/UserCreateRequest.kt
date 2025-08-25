package com.flownews.api.user.app

import com.flownews.api.user.infra.enums.OAuthProvider

data class UserCreateRequest(
    val oauthId: String,
    val email: String,
    val name: String,
    val profileUrl: String?,
    val provider: OAuthProvider,
)
