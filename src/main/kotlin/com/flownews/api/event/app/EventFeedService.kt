package com.flownews.api.event.app

import com.flownews.api.event.domain.Event
import com.flownews.api.event.domain.EventRepository
import com.flownews.api.interaction.domain.InteractionType
import com.flownews.api.interaction.domain.UserEventInteractionRepository
import com.flownews.api.reaction.domain.ReactionRepository
import com.flownews.api.recommendation.app.RecommendationApiClient
import com.flownews.api.recommendation.app.RecommendationRequest
import com.flownews.api.recommendation.app.UserEventInteractionIn
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.domain.User
import org.springframework.stereotype.Service

@Service
class EventFeedService(
    private val eventRepository: EventRepository,
    private val reactionRepository: ReactionRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
    private val userEventInteractionRepository: UserEventInteractionRepository,
    private val recommendationApiClient: RecommendationApiClient
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
        val userId = user.requireId()

        try {
            // 사용자 상호작용 히스토리 조회
            val userInteractions =
                userEventInteractionRepository.findByUserIdAndInteractionType(userId, InteractionType.VIEWED)
                    .take(100) // 최근 100개 상호작용만 사용
                    .map { interaction ->
                        UserEventInteractionIn(
                            eventId = interaction.event.id!!,
                            interactionType = mapInteractionType(interaction.interactionType),
                            createdAt = interaction.createdAt
                        )
                    }

            // 상호작용 히스토리가 있으면 추천 API 호출
            if (userInteractions.isNotEmpty()) {
                val recommendationRequest = RecommendationRequest(
                    userId = userId,
                    interactions = userInteractions,
                    numRecommendedEvents = 50
                )

                val recommendedEventIds = recommendationApiClient.getRecommendedEvents(recommendationRequest)
                    ?.recommendedEventIds ?: emptyList()

                return EventFeedResponse(recommendedEventIds)
            }
        } catch (e: Exception) {
            // 추천 API 호출 실패 시 로그 기록하고 기본 피드로 fallback
            println("Recommendation API failed: ${e.message}, falling back to default feed")
        }

        // 추천 실패 시 기본 피드 반환 (시간순 정렬)
        val allEvents = eventRepository.findAll()
        val eventIds = allEvents
            .sortedByDescending { it.eventTime }
            .mapNotNull { it.id }

        return EventFeedResponse(eventIds)
    }

    private fun mapInteractionType(interactionType: InteractionType): String {
        return when (interactionType) {
            InteractionType.VIEWED -> "VIEWED"
            InteractionType.ARTICLE_CLICKED -> "ARTICLE_CLICKED"
            InteractionType.TOPIC_VIEWED -> "TOPIC_VIEWED"
            InteractionType.TOPIC_FOLLOWED -> "TOPIC_FOLLOWED"
        }
    }
}