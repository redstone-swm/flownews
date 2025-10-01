package com.flownews.api.reaction.app

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "반응 통계 응답")
data class ReactionSummaryResponse(
    @Schema(description = "반응 타입 ID", example = "1")
    val reactionTypeId: Long,
    @Schema(description = "반응 타입 이름", example = "좋아요")
    val reactionTypeName: String,
    @Schema(description = "반응 개수", example = "10")
    val count: Long,
    @Schema(description = "사용자가 이 반응을 눌렀는지 여부", example = "true")
    val isActive: Boolean = false
)

//data class EventWithReactionsResponse(
//    val id: Long,
//    val title: String,
//    val description: String,
//    val imageUrl: String,
//    val eventTime: String,
//    val viewCount: Long,
//    val reactions: List<ReactionSummaryResponse>
//)