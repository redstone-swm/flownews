package com.flownews.api.topic.app

import com.flownews.api.event.app.EventSummaryResponse
import com.flownews.api.reaction.domain.ReactionRepository
import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicHistory

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
            reactionRepository: ReactionRepository
        ): TopicDetailsResponse = fromEntity(topic, randomTopics, null, reactionRepository)

        fun fromEntity(
            topic: Topic,
            randomTopics: List<Topic>,
            topicHistory: TopicHistory?,
            reactionRepository: ReactionRepository
        ) = TopicDetailsResponse(
            id = topic.requireId(),
            title = topic.title,
            description = topic.description,
            imageUrl = topic.imageUrl,
            events = topic.topicEvents.map { it.event }.map { EventSummaryResponse.fromEntity(it, reactionRepository) },
            recommendTopics = randomTopics.map(TopicSummaryResponse::fromEntity),
            lastReadEvent = topicHistory?.eventId,
        )
    }
}

