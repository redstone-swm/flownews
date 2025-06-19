package com.flownews.api.topic.app

data class TopicSummaryResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val imageUrl: String?
) {
    companion object {
        fun fromEntity(topic: com.flownews.api.topic.domain.Topic): TopicSummaryResponse {
            return TopicSummaryResponse(
                id = topic.id!!,
                title = topic.title,
                description = topic.description,
                imageUrl = topic.imageUrl
            )
        }
    }
}