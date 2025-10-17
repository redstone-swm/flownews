package com.flownews.api.common.api

import org.springframework.security.core.annotation.AuthenticationPrincipal

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@AuthenticationPrincipal(
    expression = "#this instanceof T(com.flownews.api.user.infra.CustomOAuth2User) ? #this.getUser() : null",
    errorOnInvalidType = false,
)
annotation class CurrentUser
