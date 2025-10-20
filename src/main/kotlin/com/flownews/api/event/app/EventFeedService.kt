package com.flownews.api.event.app

import com.flownews.api.event.domain.Event
import com.flownews.api.event.domain.EventRepository
import com.flownews.api.event.infra.RecommendationApiClient
import com.flownews.api.interaction.domain.InteractionType
import com.flownews.api.interaction.domain.UserEventInteractionRepository
import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.domain.User
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EventFeedService(
    private val eventRepository: EventRepository,
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
    private val userEventInteractionRepository: UserEventInteractionRepository,
    private val recommendationApiClient: RecommendationApiClient,
) {
    fun getUserEventFeedIds(
        user: User?,
        category: String?,
    ): EventFeedResponse {
        if (user == null) {
            val minusOneDay = LocalDateTime.now().minusDays(1).toLocalDate()
            // 비회원 전용 피드: 지난 24시간 동안 가장 인기 있는 토픽의 이벤트 (카테고리 필터링 포함)
            topicRepository.findTopKTopicsByInteractionsSince(minusOneDay, category, 5).let { topTopics ->
                val eventIds =
                    topTopics
                        .mapNotNull { it.getLastEvent() }
                        .map { it.requireId() }
                return EventFeedResponse(eventIds)
            }
        }

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
        val excludeEventIds =
            userEventInteractionRepository.findEventIdsByUserIdAndInteractionTypeOrderByCreatedAtDesc(
                userId,
                InteractionType.VIEWED,
                PageRequest.of(0, 100),
            )
        val eventIds =
            recommendationApiClient.getRecommendedEvents(
                userId = userId,
                category = category,
                excludeEventIds = excludeEventIds,
            )
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
