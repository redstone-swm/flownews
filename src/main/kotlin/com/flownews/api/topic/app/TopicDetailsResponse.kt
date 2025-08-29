package com.flownews.api.topic.app

import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicHistory
import com.flownews.api.topic.domain.event.Event
import java.time.LocalDateTime

data class TopicDetailsResponse(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val lastReadEvent: Long? = null,
    val events: List<EventSummaryResponse>,
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
            events = topic.events.map(EventSummaryResponse::fromEntity),
            recommendTopics = randomTopics.map(TopicSummaryResponse::fromEntity),
            lastReadEvent = topicHistory?.eventId,
        )
    }
}

data class EventSummaryResponse(
    var id: Long,
    var title: String,
    var description: String,
    var imageUrl: String,
    var eventTime: LocalDateTime,
    var relatedLinks: List<String>,
) {
    companion object {
        fun fromEntity(e: Event) =
            EventSummaryResponse(
                id = e.id ?: throw IllegalStateException("Event ID cannot be null"),
                title = e.title,
                description = e.description,
                imageUrl = e.imageUrl,
                eventTime = e.eventTime,
                relatedLinks = e.getRelatedLinks(),
            )
    }
}
