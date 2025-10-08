package com.flownews.api.topic.app

import com.flownews.api.event.app.EventSummaryResponse
import com.flownews.api.reaction.domain.ReactionRepository
import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.domain.User
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "토픽 상세 조회 응답")
data class TopicQueryResponse(
    @Schema(description = "토픽 고유 ID", example = "1")
    val id: Long,
    @Schema(description = "토픽 제목", example = "AI 기술 동향")
    val title: String,
    @Schema(description = "토픽 설명", example = "최신 인공지능 기술 발전과 산업 적용 사례를 다루는 토픽입니다.")
    val description: String,
    @Schema(description = "토픽에 포함된 이벤트 목록")
    val events: List<EventSummaryResponse>,
    @Schema(description = "사용자의 토픽 팔로우 여부", example = "true")
    val isFollowing: Boolean,
) {
    companion object {
        fun of(
            topic: Topic,
            isFollowing: Boolean,
            reactionRepository: ReactionRepository,
            topicSubscriptionRepository: TopicSubscriptionRepository,
            user: User?,
        ) = TopicQueryResponse(
            id = topic.requireId(),
            title = topic.title,
            description = topic.description,
            events = topic.getEvents().map { event ->
                EventSummaryResponse.fromEntity(
                    e = event,
                    reactionRepository = reactionRepository,
                    user = user,
                    topicSubscriptionRepository = topicSubscriptionRepository,
                )
            },
            isFollowing = isFollowing,
        )
    }
}
