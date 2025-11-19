package com.flownews.api.event.app

import com.flownews.api.event.domain.ReactedEvent
import com.flownews.api.topic.domain.Topic
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "이벤트 요약 정보")
data class EventListQueryResponse(
    @Schema(description = "이벤트 ID", example = "1")
    val id: Long,
    @Schema(description = "토픽")
    val topic: TopicSimpleInfo,
    @Schema(description = "이벤트 제목", example = "2025년 9월 8일 손흥민 골")
    val title: String,
    @Schema(description = "피드에 노출될 이벤트 요약 설명", example = "손흥민 선수가 중요한 경기에서 멋진 골을 넣었습니다.")
    val description: String,
    @Schema(description = "이벤트 이미지 URL", example = "https://example.com/image.jpg")
    val imageUrl: String,
    @Schema(description = "이벤트 발생 일시", example = "2025-09-09T10:30:00")
    val eventTime: LocalDateTime,
    @Schema(description = "반응 통계")
    val likeCount: Long,
) {
    companion object {
        fun from(reactedEvent: ReactedEvent) =
            reactedEvent.event.run {
                EventListQueryResponse(
                    id = requireId(),
                    title = title,
                    description = description,
                    imageUrl = imageUrl,
                    eventTime = eventTime,
                    topic = TopicSimpleInfo(getFirstTopic()),
                    likeCount = reactedEvent.event.getReactionCount(),
                )
            }
    }
}

data class TopicSimpleInfo(val id: Long, val title: String) {
    constructor(topic: Topic) : this(
        id = topic.requireId(),
        title = topic.title,
    )
}
