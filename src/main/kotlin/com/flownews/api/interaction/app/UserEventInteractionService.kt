package com.flownews.api.interaction.app

import com.flownews.api.event.domain.EventRepository
import com.flownews.api.interaction.domain.InteractionType
import com.flownews.api.interaction.domain.UserEventInteraction
import com.flownews.api.interaction.domain.UserEventInteractionRepository
import com.flownews.api.interaction.infra.EventBasedProfileUpdateRequest
import com.flownews.api.interaction.infra.UserProfileApiClient
import com.flownews.api.user.domain.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserEventInteractionService(
    private val userEventInteractionRepository: UserEventInteractionRepository,
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
    private val userProfileApiClient: UserProfileApiClient,
) {
    fun recordInteraction(
        userId: Long,
        eventId: Long,
        interactionType: InteractionType,
        additionalData: String? = null,
    ): UserEventInteraction {
        val user =
            userRepository
                .findById(userId)
                .orElseThrow { IllegalArgumentException("User not found with id: $userId") }

        val event =
            eventRepository
                .findById(eventId)
                .orElseThrow { IllegalArgumentException("Event not found with id: $eventId") }

        val interaction =
            UserEventInteraction(
                user = user,
                event = event,
                interactionType = interactionType,
                additionalData = additionalData,
            )

        userProfileApiClient.updateProfileByEvent(
            userId = userId,
            request =
                EventBasedProfileUpdateRequest(
                    userId = userId,
                    eventIds = listOf(eventId),
                    action = interactionType,
                ),
        )

        return userEventInteractionRepository.save(interaction)
    }
}
