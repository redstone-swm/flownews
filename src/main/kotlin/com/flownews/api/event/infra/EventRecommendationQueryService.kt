package com.flownews.api.event.infra

import com.flownews.api.event.domain.Event
import com.flownews.api.event.domain.EventRepository
import com.flownews.api.interaction.domain.InteractionType
import com.flownews.api.interaction.domain.UserEventInteractionRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class EventRecommendationQueryService(
    private val eventRecommendationApiClient: EventRecommendationApiClient,
    private val userEventInteractionRepository: UserEventInteractionRepository,
    private val eventRepository: EventRepository,
) {
    private val logger = LoggerFactory.getLogger(EventRecommendationQueryService::class.java)

    fun getRecommendedEvents(
        userId: Long,
        category: String? = null,
        k: Long = 5,
    ): List<Event> =
        try {
            val req =
                EventRecommendationQueryRequest(
                    userId = userId,
                    category = category,
                    k = k,
                    excludeEventIds = getExcludeEventIds(userId),
                )

            val response = eventRecommendationApiClient.getRecommendedEvents(req)
            val eventIds = response.events.map { it.eventId }

            eventRepository.findAllById(eventIds).toList()
        } catch (e: Exception) {
            logger.error("Error calling recommendation API: ${e.message}", e)
            emptyList()
        }

    private fun getExcludeEventIds(userId: Long): List<Long> {
        return userEventInteractionRepository
            .findEventIdsByUserIdAndInteractionTypeOrderByCreatedAtDesc(
                userId,
                InteractionType.VIEWED,
                PageRequest.of(0, 20),
            )
    }
}
