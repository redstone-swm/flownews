package com.flownews.api.user.app

import com.flownews.api.user.infra.enums.OAuthProvider
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 생성 요청")
data class UserCreateRequest(
    @Schema(description = "OAuth 고유 ID", example = "1234567890abcdef")
    val oauthId: String,
    @Schema(description = "이메일", example = "user@example.com")
    val email: String,
    @Schema(description = "이름", example = "홍길동")
    val name: String,
    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg", nullable = true)
    val profileUrl: String?,
    @Schema(description = "OAuth 제공자", example = "GOOGLE")
    val provider: OAuthProvider,
)
