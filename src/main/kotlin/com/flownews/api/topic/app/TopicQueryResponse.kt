package com.flownews.api.topic.app

import com.flownews.api.event.app.article.ArticleResponse
import com.flownews.api.event.domain.ReactedEvent
import com.flownews.api.reaction.app.ReactionSummaryResponse
import com.flownews.api.topic.domain.FollowedTopic
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "토픽 상세 조회 응답")
data class TopicQueryResponse(
    @Schema(description = "토픽 고유 ID", example = "1")
    val id: Long,
    @Schema(description = "토픽 제목", example = "AI 기술 동향")
    val title: String,
    @Schema(description = "토픽 설명", example = "최신 인공지능 기술 발전과 산업 적용 사례를 다루는 토픽입니다.")
    val description: String,
    @Schema(description = "토픽에 포함된 이벤트 목록")
    val events: List<EventItemQueryResponse>,
    @Schema(description = "사용자의 토픽 팔로우 여부", example = "true")
    val isFollowing: Boolean,
) {
    companion object {
        fun of(
            followedTopic: FollowedTopic,
            events: List<ReactedEvent>,
        ) = followedTopic.run {
            TopicQueryResponse(
                id = topic.requireId(),
                title = topic.title,
                description = topic.description,
                events = events.map { it.toEventItemQueryResponse() },
                isFollowing = isFollowed,
            )
        }
    }
}

@Schema(description = "이벤트 요약 정보")
data class EventItemQueryResponse(
    @Schema(description = "이벤트 ID", example = "1")
    val id: Long,
    @Schema(description = "이벤트 제목", example = "2025년 9월 8일 손흥민 골")
    val title: String,
    @Schema(description = "피드에 노출될 이벤트 요약 설명", example = "손흥민 선수가 중요한 경기에서 멋진 골을 넣었습니다.")
    val description: String,
    @Schema(description = "이벤트 이미지 URL", example = "https://example.com/image.jpg")
    val imageUrl: String,
    @Schema(description = "이벤트 발생 일시", example = "2025-09-09T10:30:00")
    val eventTime: LocalDateTime,
    @Schema(description = "관련 기사 목록")
    val articles: List<ArticleResponse>,
    @Schema(description = "반응 통계")
    val reactions: List<ReactionSummaryResponse>,
)

fun ReactedEvent.toEventItemQueryResponse() =
    EventItemQueryResponse(
        id = event.requireId(),
        title = event.title,
        description = event.description,
        imageUrl = event.imageUrl,
        eventTime = event.eventTime,
        articles = event.articles.map { ArticleResponse.fromEntity(it) },
        reactions = listOf(ReactionSummaryResponse.from(this)),
    )
