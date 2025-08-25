package com.flownews.api.auth.app.enums

enum class OAuthProvider {
    GOOGLE,
    APPLE,
    ;

    companion object {
        fun from(registrationId: String): OAuthProvider =
            when (registrationId.lowercase()) {
                "google" -> GOOGLE
                "apple" -> APPLE
                else -> throw IllegalArgumentException("Unknown provider: $registrationId")
            }
    }
}
