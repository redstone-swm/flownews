package com.flownews.api.topic.app

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "토픽 구독 토글 응답")
data class TopicSubscriptionToggleResponse(
    @Schema(description = "구독 상태", example = "true")
    val isSubscribed: Boolean,
    @Schema(description = "응답 메시지", example = "토픽을 구독했습니다.")
    val message: String,
) {
    constructor(isSubscribed: Boolean) : this(
        isSubscribed = isSubscribed,
        message = if (isSubscribed) "토픽을 구독했습니다." else "토픽 구독을 취소했습니다.",
    )
}
