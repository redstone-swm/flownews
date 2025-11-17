package com.flownews.api.user.infra

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
        val profile = OAuthUserProfile.of(oauth2User, provider)

        val user = getUser(profile) ?: createUser(profile)

        return CustomOAuth2User(oauth2User.attributes, user)
    }

    private fun getUser(profile: OAuthUserProfile): User? =
        userRepository.findByOauthIdAndProvider(
            profile.oauthId,
            profile.provider.name,
        )

    private fun createUser(profile: OAuthUserProfile): User =
        userCreateService.createNewUser(profile.toUserCreateRequest())
}
