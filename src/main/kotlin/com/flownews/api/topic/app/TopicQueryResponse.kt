package com.flownews.api.topic.app

import com.flownews.api.topic.domain.Topic
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
    val events: List<EventItemQueryResponse>,
    @Schema(description = "사용자의 토픽 팔로우 여부", example = "true")
    val isFollowing: Boolean,
) {
    companion object {
        fun of(
            topic: Topic,
            isFollowing: Boolean,
            reactedEventIds: Set<Long> = emptySet(),
        ) = TopicQueryResponse(
            id = topic.requireId(),
            title = topic.title,
            description = topic.description,
            events =
                topic.getEvents().map { event ->
                    val eventId = event.requireId()
                    EventItemQueryResponse(
                        id = eventId,
                        title = event.title,
                        content = event.description,
                        imageUrl = event.imageUrl,
                        eventTime = event.eventTime.toString(),
                        likeCount = event.totalReactionsCount,
                        isLiked = reactedEventIds.contains(eventId),
                    )
                },
            isFollowing = isFollowing,
        )
    }
}

@Schema(description = "이벤트 항목 정보")
data class EventItemQueryResponse(
    @Schema(description = "이벤트 고유 ID", example = "101")
    val id: Long,
    @Schema(description = "이벤트 제목", example = "OpenAI GPT-4 출시")
    val title: String,
    @Schema(description = "이벤트 내용", example = "OpenAI에서 차세대 언어모델 GPT-4를 공식 출시했습니다.")
    val content: String,
    @Schema(description = "이벤트 이미지 URL", example = "https://example.com/image1.jpg")
    val imageUrl: String,
    @Schema(description = "이벤트 발생 시간", example = "2024-03-15T10:30:00")
    val eventTime: String,
    @Schema(description = "좋아요 수", example = "245")
    val likeCount: Long,
    @Schema(description = "사용자의 좋아요 여부", example = "true")
    val isLiked: Boolean,
)
