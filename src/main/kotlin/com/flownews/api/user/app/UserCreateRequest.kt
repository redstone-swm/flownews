package com.flownews.api.user.app

import com.flownews.api.auth.app.enums.OAuthProvider

data class UserCreateRequest(
    val oauthId: String,
    val email: String,
    val name: String,
    val profileUrl: String?,
    val provider: OAuthProvider,
)
