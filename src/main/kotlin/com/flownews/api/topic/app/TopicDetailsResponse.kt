package com.flownews.api.topic.app

import com.flownews.api.event.app.EventSummaryResponse
import com.flownews.api.reaction.domain.ReactionRepository
import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicHistory
import com.flownews.api.user.domain.User

data class TopicDetailsResponse(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val isFollowing: Boolean = false,
    val lastReadEvent: Long? = null,
    val events: List<EventSummaryResponse>,
    val recommendTopics: List<TopicSummaryResponse>,
) {
    companion object {
        fun fromEntity(
            topic: Topic,
            recommendTopics: List<TopicSummaryResponse>,
            topicHistory: TopicHistory?,
            reactionRepository: ReactionRepository,
            isFollowing: Boolean,
            user: User?,
            topicSubscriptionRepository: com.flownews.api.topic.domain.TopicSubscriptionRepository
        ) = TopicDetailsResponse(
            id = topic.requireId(),
            title = topic.title,
            description = topic.description,
            imageUrl = topic.imageUrl,
            isFollowing = isFollowing,
            events = topic.topicEvents.map { it.event }.map {
                EventSummaryResponse.fromEntity(it, reactionRepository, user, topicSubscriptionRepository)
            },
            recommendTopics = recommendTopics,
            lastReadEvent = topicHistory?.eventId,
        )
    }
}

