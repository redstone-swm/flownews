package com.flownews.api.reaction.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ReactionRepository : JpaRepository<Reaction, Long> {
    fun findByUserIdAndEventIdAndIsDeletedIsNull(
        userId: Long,
        eventId: Long,
    ): Reaction?

    @Query(
        """
        SELECT COUNT(r) 
        FROM Reaction r 
        WHERE r.event.id = :eventId 
        AND r.reactionType.id = :reactionTypeId 
        AND r.isDeleted IS NULL
        """,
    )
    fun countByEventIdAndReactionTypeIdAndIsDeletedIsNull(
        eventId: Long,
        reactionTypeId: Long,
    ): Long

    fun existsByUserIdAndEventIdAndReactionTypeId(
        userId: Long,
        eventId: Long,
        reactionTypeId: Long = 1L,
    ): Boolean
}
