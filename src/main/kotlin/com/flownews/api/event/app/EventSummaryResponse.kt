package com.flownews.api.event.app

import com.flownews.api.article.app.ArticleResponse
import com.flownews.api.event.domain.Event
import com.flownews.api.reaction.app.ReactionSummaryResponse
import com.flownews.api.reaction.domain.ReactionRepository
import com.flownews.api.topic.app.TopicSummaryResponse
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.domain.User
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "이벤트 요약 정보")
data class EventSummaryResponse(
    @Schema(description = "이벤트 ID", example = "1")
    var id: Long,
    @Schema(description = "이벤트 제목", example = "2025년 9월 8일 손흥민 골")
    var title: String,
    @Schema(description = "피드에 노출될 이벤트 요약 설명", example = "손흥민 선수가 중요한 경기에서 멋진 골을 넣었습니다.")
    var description: String,
    @Schema(description = "이벤트 이미지 URL", example = "https://example.com/image.jpg")
    var imageUrl: String,
    @Schema(description = "이벤트 발생 일시", example = "2025-09-09T10:30:00")
    var eventTime: LocalDateTime,
    @Schema(description = "토픽")
    var topics: List<TopicSummaryResponse>,
    @Schema(description = "관련 기사 목록")
    var articles: List<ArticleResponse>,
    @Schema(description = "반응 통계")
    var reactions: List<ReactionSummaryResponse>,
) {
    companion object {
        fun fromEntity(
            e: Event,
            reactionRepository: ReactionRepository,
            user: User?,
            topicSubscriptionRepository: TopicSubscriptionRepository,
        ) = EventSummaryResponse(
            id = e.id ?: throw IllegalStateException("Event ID cannot be null"),
            title = e.title,
            description = e.description,
            imageUrl = e.imageUrl,
            eventTime = e.eventTime,
            topics =
                if (user != null) {
                    val userId = user.requireId()
                    e.topicEvents.map { topicEvent ->
                        val isFollowing =
                            topicSubscriptionRepository.existsByTopicIdAndUserId(topicEvent.topic.requireId(), userId)
                        TopicSummaryResponse.fromEntity(topicEvent.topic, isFollowing)
                    }
                } else {
                    e.topicEvents.map { TopicSummaryResponse.fromEntity(it.topic) }
                },
            articles = e.articles.map { ArticleResponse.fromEntity(it) },
            reactions =
                if (user != null) {
                    reactionRepository
                        .findReactionCountsByEventIdAndUserId(
                            e.requireId(),
                            user.requireId(),
                        ).map { reaction ->
                            ReactionSummaryResponse(
                                reactionTypeId = reaction.reactionTypeId,
                                reactionTypeName = reaction.reactionTypeName,
                                count = reaction.count + e.viewCount,
                                isActive = reaction.active,
                            )
                        }
                } else {
                    reactionRepository.findReactionCountsByEventId(e.requireId()).map { reaction ->
                        ReactionSummaryResponse(
                            reactionTypeId = reaction.reactionTypeId,
                            reactionTypeName = reaction.reactionTypeName,
                            count = reaction.count + e.viewCount,
                            isActive = false,
                        )
                    }
                },
        )
    }
}
