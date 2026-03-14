package com.flownews.testutils

import com.flownews.api.common.api.CurrentUser
import com.flownews.api.user.domain.User
import com.flownews.api.user.domain.enums.Role
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class MockCurrentUserArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(CurrentUser::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): User {
        return User(
            id = 1L,
            oauthId = "test-user-123",
            provider = "GOOGLE",
            name = "Test User",
            email = "test@example.com",
            profileUrl = null,
            role = Role.USER,
            deviceToken = "test-token",
        )
    }
}
