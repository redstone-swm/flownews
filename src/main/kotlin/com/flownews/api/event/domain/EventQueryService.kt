package com.flownews.api.event.domain

import com.flownews.api.common.app.NoDataException
import com.flownews.api.reaction.domain.ReactionRepository
import com.flownews.api.user.domain.User
import org.springframework.stereotype.Service

@Service
class EventQueryService(
    private val eventRepository: EventRepository,
    private val reactionRepository: ReactionRepository,
) {
    fun getReactedEvent(
        id: Long,
        user: User?,
    ): ReactedEvent {
        val event =
            eventRepository.findById(id).orElseThrow {
                NoDataException("Event not found: $id")
            }
        if (user == null) {
            return ReactedEvent(event, false)
        }

        val isReacted = reactionRepository.existsByUserIdAndEventIdAndReactionTypeId(id, user.requireId())

        return ReactedEvent(event, isReacted)
    }
}
