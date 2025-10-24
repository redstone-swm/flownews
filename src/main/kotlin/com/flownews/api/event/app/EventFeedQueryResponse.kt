package com.flownews.api.event.app

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "이벤트 피드 응답")
data class EventFeedQueryResponse(
    @Schema(description = "이벤트 ID 목록", example = "[1, 2, 3, 4, 5]")
    val eventIds: List<Long>,
)
