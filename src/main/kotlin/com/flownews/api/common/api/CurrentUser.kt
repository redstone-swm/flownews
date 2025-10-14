package com.flownews.api.common.api

import org.springframework.core.annotation.AliasFor
import org.springframework.security.core.annotation.AuthenticationPrincipal

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@AuthenticationPrincipal(expression = "getUser()")
annotation class CurrentUser(
    @get:AliasFor(annotation = AuthenticationPrincipal::class, attribute = "errorOnInvalidType")
    val errorOnInvalidType: Boolean = true,
)
