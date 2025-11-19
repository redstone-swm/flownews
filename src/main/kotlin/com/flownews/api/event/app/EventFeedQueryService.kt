package com.flownews.api.event.app

import com.flownews.api.event.domain.Event
import com.flownews.api.event.domain.EventRepository
import com.flownews.api.event.infra.RecommendationApiClient
import com.flownews.api.interaction.domain.InteractionType
import com.flownews.api.interaction.domain.UserEventInteractionRepository
import com.flownews.api.topic.app.TopicListQueryService
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.domain.User
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class EventFeedQueryService(
    private val eventRepository: EventRepository,
    private val topicListQueryService: TopicListQueryService,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
    private val userEventInteractionRepository: UserEventInteractionRepository,
    private val recommendationApiClient: RecommendationApiClient,
) {
    fun getEventFeeds(
        user: User?,
        category: String?,
    ): List<Event> =
        if (user == null) {
            guestFeed(category)
        } else {
            personalizedFeed(user.requireId(), category)
        }

    private fun guestFeed(category: String?): List<Event> =
        topicListQueryService
            .findTopTopicsSinceLast24Hours(5, category)
            .map { it.getLastEvent() }

    private fun personalizedFeed(
        userId: Long,
        category: String?,
    ) = getSubscribedLastEvents(userId)
        .union(getRecommendedEvents(userId, category))
        .toList()

    private fun getRecommendedEvents(
        userId: Long,
        category: String?,
    ): List<Event> {
        val excludeEventIds =
            userEventInteractionRepository.findEventIdsByUserIdAndInteractionTypeOrderByCreatedAtDesc(
                userId,
                InteractionType.VIEWED,
                PageRequest.of(0, 20),
            )
        val eventIds =
            recommendationApiClient.getRecommendedEvents(
                userId = userId,
                category = category,
                excludeEventIds = excludeEventIds,
            )
        return eventRepository.findAllById(eventIds).filter { !it.isNoise }.toList()
    }

    private fun getSubscribedLastEvents(userId: Long): List<Event> =
        topicSubscriptionRepository
            .findByUserId(userId)
            .filter { !it.topic.isNoise }
            .map { subscription -> subscription.topic.getLastEvent() }
            .filter { event ->
                !event.isNoise &&
                    userEventInteractionRepository.findByUserIdAndEventId(userId, event.requireId()).isEmpty()
            }
}
