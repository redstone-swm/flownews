package com.flownews.api.reaction.app

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "반응 토글 응답")
data class ReactionToggleResponse(
    @Schema(description = "반응 활성화 상태", example = "true")
    val isActive: Boolean,
    @Schema(description = "응답 메시지", example = "좋아요 반응이 추가되었습니다.")
    val message: String
)