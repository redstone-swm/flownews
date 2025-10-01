package com.flownews.api.user.infra

import com.flownews.api.user.app.UserCreateRequest
import com.flownews.api.user.app.UserCreateService
import com.flownews.api.user.domain.User
import com.flownews.api.user.domain.UserRepository
import com.flownews.api.user.infra.enums.OAuthProvider
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuth2UserService(
    private val userRepository: UserRepository,
    private val userCreateService: UserCreateService,
) : DefaultOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oauth2User = super.loadUser(userRequest)
        val provider = OAuthProvider.from(userRequest.clientRegistration.registrationId)
        val info = OAuthUserProfile.of(oauth2User, provider)

        val user =
            getOrCreateUser(
                oauthId = info.oauthId,
                email = info.email,
                name = info.name,
                profileUrl = info.profileUrl,
                provider = info.provider,
            )

        return CustomOAuth2User(oauth2User.attributes, user)
    }

    private fun getOrCreateUser(
        oauthId: String,
        email: String,
        name: String,
        profileUrl: String?,
        provider: OAuthProvider,
    ): User {
        val providerKey = provider.name
        return userRepository.findByOauthIdAndProvider(oauthId, providerKey)
            ?: userCreateService.createNewUser(
                UserCreateRequest(
                    oauthId = oauthId,
                    email = email,
                    name = name,
                    profileUrl = profileUrl,
                    provider = provider,
                    // OAuth에서는 제공되지 않으므로 null로 설정
                    birthDate = null,
                    // OAuth에서는 제공되지 않으므로 null로 설정
                    gender = null,
                ),
            )
    }
}

private data class OAuthUserProfile(
    val oauthId: String,
    val email: String,
    val name: String,
    val profileUrl: String?,
    val provider: OAuthProvider,
) {
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
