package com.flownews.api.user.app

import com.flownews.api.user.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User(
    private val attributes: Map<String, Any?>,
    private val user: User
) : OAuth2User {
    override fun getAttributes(): Map<String, Any?> = attributes

    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_USER"))

    override fun getName(): String = user.id?.toString() ?: "anonymous"

    fun getUser(): User = user
}