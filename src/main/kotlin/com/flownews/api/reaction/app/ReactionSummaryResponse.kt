package com.flownews.api.reaction.app

import com.flownews.api.event.domain.ReactedEvent
import com.flownews.api.reaction.domain.ReactionType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "반응 통계 응답")
data class ReactionSummaryResponse(
    @Schema(description = "반응 타입 ID", example = "1")
    val reactionType: ReactionType,
    @Schema(description = "반응 개수", example = "10")
    val count: Long,
    @Schema(description = "사용자가 이 반응을 눌렀는지 여부", example = "true")
    val isActive: Boolean = false,
) {
    companion object {
        fun from(reactedEvent: ReactedEvent): ReactionSummaryResponse {
            return ReactionSummaryResponse(
                reactionType = ReactionType.LIKE,
                count = reactedEvent.event.getReactionCount(),
                isActive = reactedEvent.isReacted,
            )
        }
    }
}
