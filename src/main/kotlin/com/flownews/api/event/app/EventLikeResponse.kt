package com.flownews.api.event.app

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "이벤트 좋아요 응답")
data class EventLikeResponse(
    @Schema(description = "좋아요 활성화 상태", example = "true")
    val isActive: Boolean,
    @Schema(description = "응답 메시지", example = "좋아요가 추가되었습니다.")
    val message: String,
    @Schema(description = "해당 이벤트의 총 좋아요 개수", example = "5")
    val count: Long,
) {
    companion object {
        fun ok() {
            EventLikeResponse(
                isActive = true,
                message = "좋아요가 추가되었습니다.",
                count = 0L,
            )
        }
    }
}
