package com.flownews.api.interaction.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface InteractionRepository : JpaRepository<Interaction, Long> {
    fun findByUserIdAndEventId(
        userId: Long,
        eventId: Long,
    ): List<Interaction>

    @Query(
        "SELECT uei.event.id FROM Interaction uei " +
            "WHERE uei.user.id = :userId AND uei.interactionType = :interactionType " +
            "ORDER BY uei.createdAt DESC",
    )
    fun findEventIdsByUserIdAndInteractionTypeOrderByCreatedAtDesc(
        @Param("userId") userId: Long,
        @Param("interactionType") interactionType: InteractionType,
        pageable: org.springframework.data.domain.Pageable,
    ): List<Long>
}
