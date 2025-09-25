package com.flownews.api.topic.app

import com.flownews.api.topic.domain.Topic
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "토픽 요약 정보")
data class TopicSummaryResponse(
    @Schema(description = "토픽 ID", example = "1")
    val id: Long,
    @Schema(description = "토픽 제목", example = "손흥민")
    val title: String,
    @Schema(description = "토픽 설명", example = "토트넘 홋스퍼 FC의 대한민국 축구 선수")
    val description: String,
    @Schema(description = "토픽 이미지 URL", example = "https://example.com/topic-image.jpg")
    val imageUrl: String,
    @Schema(description = "사용자가 이 토픽을 팔로우하는지 여부", example = "false")
    val isFollowing: Boolean = false,
) {
    companion object {
        fun fromEntity(topic: Topic, isFollowing: Boolean = false) = TopicSummaryResponse(
            id = topic.id ?: throw IllegalStateException("Topic ID cannot be null"),
            title = topic.title,
            description = topic.description,
            imageUrl = topic.imageUrl,
            isFollowing = isFollowing
        )
    }
}
