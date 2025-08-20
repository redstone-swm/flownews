package com.flownews.api.topic.app

import com.flownews.api.event.app.EventDto
import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicHistory

data class TopicDetailsResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val lastReadEvent: Long? = null,
    val events: List<EventDto>,
    val recommendTopics: List<TopicSummaryResponse>,
) {
    companion object {
        fun fromEntity(
            topic: Topic,
            randomTopics: List<Topic>,
        ): TopicDetailsResponse = fromEntity(topic, randomTopics, null)

        fun fromEntity(
            topic: Topic,
            randomTopics: List<Topic>,
            topicHistory: TopicHistory?,
        ) = TopicDetailsResponse(
            id = topic.requireId(),
            title = topic.title,
            description = topic.description,
            imageUrl = topic.imageUrl,
            events = topic.events.map(EventDto::fromEntity),
            recommendTopics = randomTopics.map(TopicSummaryResponse::fromEntity),
            lastReadEvent = topicHistory?.eventId,
        )
    }
}
