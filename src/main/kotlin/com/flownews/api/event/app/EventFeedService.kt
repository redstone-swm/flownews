package com.flownews.api.event.app

import com.flownews.api.event.domain.Event
import com.flownews.api.event.domain.EventRepository
import com.flownews.api.reaction.domain.ReactionRepository
import com.flownews.api.user.domain.User
import org.springframework.stereotype.Service

@Service
class EventFeedService(
    private val eventRepository: EventRepository,
    private val reactionRepository: ReactionRepository
) {
    fun getUserEventFeed(user: User): List<EventSummaryResponse> {
        // TODO: personalize event feed based on user preferences
        val allEvents = eventRepository.findAll()

        return allEvents
            .sortedByDescending { it.eventTime }
            .map { event -> 
                val topicTitle = event.topicEvents.firstOrNull()?.topic?.title
                EventSummaryResponse.fromEntity(event, reactionRepository, topicTitle)
            }
    }
}