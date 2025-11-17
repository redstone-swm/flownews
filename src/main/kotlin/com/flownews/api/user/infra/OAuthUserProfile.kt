package com.flownews.api.user.infra

import com.flownews.api.user.app.UserCreateRequest
import com.flownews.api.user.infra.enums.OAuthProvider
import org.springframework.security.oauth2.core.user.OAuth2User

data class OAuthUserProfile(
    val oauthId: String,
    val email: String,
    val name: String,
    val profileUrl: String?,
    val provider: OAuthProvider,
) {
    fun toUserCreateRequest(): UserCreateRequest = UserCreateRequest(oauthId, email, name, profileUrl, provider)

    companion object {
        fun of(
            user: OAuth2User,
            provider: OAuthProvider,
        ): OAuthUserProfile =
            when (provider) {
                OAuthProvider.GOOGLE -> fromGoogle(user)
                OAuthProvider.APPLE -> fromApple(user)
            }

        private fun fromGoogle(user: OAuth2User) =
            OAuthUserProfile(
                oauthId = user.req<String>("sub"),
                email = user.req<String>("email"),
                name = user.req<String>("name"),
                profileUrl = user.opt<String>("picture"),
                provider = OAuthProvider.GOOGLE,
            )

        private fun fromApple(user: OAuth2User) =
            OAuthUserProfile(
                oauthId = user.req<String>("sub"),
                email = user.req<String>("email"),
                name = user.opt<String>("name") ?: "Apple User",
                profileUrl = null,
                provider = OAuthProvider.APPLE,
            )
    }
}

private inline fun <reified T> OAuth2User.req(key: String): T =
    requireNotNull(getAttribute<T>(key)) { "Missing required attribute: $key" }

private inline fun <reified T> OAuth2User.opt(key: String): T? = getAttribute<T>(key)
