package com.flownews.api.event.app

import com.flownews.api.event.domain.Event
import com.flownews.api.event.domain.EventRepository
import com.flownews.api.event.infra.RecommendationApiClient
import com.flownews.api.interaction.domain.UserEventInteractionRepository
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.domain.User
import org.springframework.stereotype.Service

@Service
class EventFeedService(
    private val eventRepository: EventRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
    private val userEventInteractionRepository: UserEventInteractionRepository,
    private val recommendationApiClient: RecommendationApiClient,
) {
    fun getUserEventFeedIds(
        user: User,
        category: String?,
    ): EventFeedResponse {
        val userId = user.requireId()

        val eventIds =
            getSubscribedLastEvent(userId)
                .union(getRecommendedEvent(userId, category))
                .map { it.requireId() }

        return EventFeedResponse(eventIds)
    }

    private fun getRecommendedEvent(
        userId: Long,
        category: String?,
    ): List<Event> {
        val eventIds = recommendationApiClient.getRecommendedEvents(userId, category)
        return eventRepository.findAllById(eventIds).toList()
    }

    private fun getSubscribedLastEvent(userId: Long): List<Event> =
        topicSubscriptionRepository
            .findByUserId(userId)
            .mapNotNull { subscription -> subscription.topic.getLastEvent() }
            .filter { event ->
                userEventInteractionRepository.findByUserIdAndEventId(userId, event.requireId()).isEmpty()
            }
}
