package com.flownews.api.event.app

import com.flownews.api.event.domain.Event
import com.flownews.api.event.domain.EventRepository
import com.flownews.api.reaction.domain.ReactionRepository
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.domain.User
import com.flownews.api.user.infra.CustomOAuth2User
import org.springframework.stereotype.Service

@Service
class EventFeedService(
    private val eventRepository: EventRepository,
    private val reactionRepository: ReactionRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository
) {
    fun getUserEventFeed(user: User): List<EventSummaryResponse> {
        // TODO: personalize event feed based on user preferences
        val allEvents = eventRepository.findAll()

        return allEvents
            .sortedByDescending { it.eventTime }
            .map { event ->
                EventSummaryResponse.fromEntity(event, reactionRepository, user, topicSubscriptionRepository)
            }
    }

    fun getUserEventFeedIds(user: User): EventFeedResponse {
        // TODO: personalize event feed based on user preferences
        val allEvents = eventRepository.findAll()

        val eventIds = allEvents
            .sortedByDescending { it.eventTime }
            .mapNotNull { it.id }

        return EventFeedResponse(eventIds)
    }
}