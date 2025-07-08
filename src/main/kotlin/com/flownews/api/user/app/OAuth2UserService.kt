package com.flownews.api.user.app

import com.flownews.api.user.domain.enums.Role
import com.flownews.api.user.domain.User
import com.flownews.api.user.domain.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuth2UserService(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oauth2User = super.loadUser(userRequest)

        val registrationId = userRequest.clientRegistration.registrationId

        return when (registrationId) {
            "google" -> processGoogleUser(oauth2User)
            "apple" -> processAppleUser(oauth2User)
            else -> throw IllegalArgumentException("Unsupported provider: $registrationId")
        }
    }

    private fun processGoogleUser(oauth2User: OAuth2User): OAuth2User {
        val email = oauth2User.getAttribute<String>("email")
            ?: throw IllegalArgumentException("Email not found from Google")
        val name = oauth2User.getAttribute<String>("name")
            ?: throw IllegalArgumentException("Name not found from Google")
        val profileUrl = oauth2User.getAttribute<String>("picture")
        val oauthId = oauth2User.getAttribute<String>("sub")
            ?: throw IllegalArgumentException("OAuthId (sub) not found from Google")

        val user = userRepository.findByOauthIdAndProvider(oauthId, "google")
            ?: createNewUser(oauthId, email, name, profileUrl, "google")

        return CustomOAuth2User(oauth2User.attributes, user)
    }

    private fun processAppleUser(oauth2User: OAuth2User): OAuth2User {
        val oauthId = oauth2User.getAttribute<String>("sub")
            ?: throw IllegalArgumentException("OAuthId (sub) not found from Apple")
        val email = oauth2User.getAttribute<String>("email")
            ?: throw IllegalArgumentException("Email not found from Apple")
        // Apple은 최초 로그인 시에만 이름을 제공할 수 있음
        val name = oauth2User.getAttribute<String>("name") ?: "Apple User"
        val profileUrl: String? = null // Apple은 프로필 이미지를 제공하지 않음

        val user = userRepository.findByOauthIdAndProvider(oauthId, "apple")
            ?: createNewUser(oauthId, email, name, profileUrl, "apple")

        return CustomOAuth2User(oauth2User.attributes, user)
    }

    private fun createNewUser(
        oauthId: String,
        email: String,
        name: String,
        profileUrl: String?,
        provider: String
    ): User {
        val user = User(
            oauthId = oauthId,
            email = email,
            name = name,
            profileUrl = profileUrl,
            provider = provider,
            role = Role.USER,
        )
        return userRepository.save(user)
    }
}