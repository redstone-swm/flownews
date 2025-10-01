package com.flownews.api.interaction.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserEventInteractionRepository : JpaRepository<UserEventInteraction, Long> {
    fun findByUserIdAndEventId(
        userId: Long,
        eventId: Long,
    ): List<UserEventInteraction>

    fun findByUserIdAndInteractionType(
        userId: Long,
        interactionType: InteractionType,
    ): List<UserEventInteraction>

    @Query("SELECT uei FROM UserEventInteraction uei WHERE uei.user.id = :userId AND uei.event.id IN :eventIds")
    fun findByUserIdAndEventIds(
        @Param("userId") userId: Long,
        @Param("eventIds") eventIds: List<Long>,
    ): List<UserEventInteraction>

    @Query(
        "SELECT COUNT(uei) FROM UserEventInteraction uei " +
            "WHERE uei.event.id = :eventId AND uei.interactionType = :interactionType",
    )
    fun countByEventIdAndInteractionType(
        @Param("eventId") eventId: Long,
        @Param("interactionType") interactionType: InteractionType,
    ): Long
}
