package com.flownews.api.event.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.event.domain.EventRepository
import com.flownews.api.reaction.domain.ReactionRepository
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.domain.User
import org.springframework.stereotype.Service

@Service
class EventQueryService(
    private val eventRepository: EventRepository,
    private val reactionRepository: ReactionRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository
) {
    fun getEvent(id: Long, user: User?): EventSummaryResponse {
        val event = eventRepository.findById(id).orElseThrow { 
            NoDataException("Event not found: $id") 
        }
        
        return EventSummaryResponse.fromEntity(event, reactionRepository, user, topicSubscriptionRepository)
    }
}