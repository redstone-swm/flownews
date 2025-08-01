package com.flownews.api.topic.app

import com.flownews.api.topic.domain.Topic

data class TopicSummaryResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val imageUrl: String?
) {
    companion object {
        fun fromEntity(topic: Topic): TopicSummaryResponse {
            return TopicSummaryResponse(
                id = topic.id!!,
                title = topic.title,
                description = topic.description,
                imageUrl = topic.imageUrl
            )
        }
    }
}