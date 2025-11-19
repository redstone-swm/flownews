package com.flownews.api.topic.app

import com.flownews.api.topic.domain.Topic
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "토픽 요약 정보")
data class TopicListQueryResponse(
    @Schema(description = "토픽 ID", example = "1")
    val id: Long,
    @Schema(description = "토픽 제목", example = "손흥민")
    val title: String,
    @Schema(description = "토픽 설명", example = "토트넘 홋스퍼 FC의 대한민국 축구 선수")
    val description: String,
) {
    companion object {
        fun from(topic: Topic) =
            TopicListQueryResponse(
                id = topic.requireId(),
                title = topic.title,
                description = topic.description,
            )
    }
}
