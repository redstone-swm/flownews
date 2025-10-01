package com.flownews.api.reaction.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ReactionRepository : JpaRepository<Reaction, Long> {
    @Query(
        """
        SELECT rt.id as reactionTypeId, rt.name as reactionTypeName, COALESCE(COUNT(r), 0) as count 
        FROM ReactionType rt 
        LEFT JOIN Reaction r ON r.reactionType.id = rt.id AND r.event.id = :eventId AND r.isDeleted IS NULL 
        GROUP BY rt.id, rt.name
        ORDER BY rt.id
    """,
    )
    fun findReactionCountsByEventId(eventId: Long): List<ReactionCount>

    @Query(
        """
    SELECT rt.id as reactionTypeId,
           rt.name as reactionTypeName,
           COUNT(r.id) as count,
           (COUNT(ur.id) > 0) as active
    FROM ReactionType rt
    LEFT JOIN Reaction r
           ON r.reactionType.id = rt.id
          AND r.event.id = :eventId
          AND r.isDeleted IS NULL
    LEFT JOIN Reaction ur
           ON ur.reactionType.id = rt.id
          AND ur.event.id = :eventId
          AND ur.user.id = :userId
          AND ur.isDeleted IS NULL
    GROUP BY rt.id, rt.name
    ORDER BY rt.id
    """,
    )
    fun findReactionCountsByEventIdAndUserId(
        eventId: Long,
        userId: Long,
    ): List<ReactionCountWithActive>

    fun findByUserIdAndEventIdAndIsDeletedIsNull(
        userId: Long,
        eventId: Long,
    ): Reaction?
}

interface ReactionCount {
    val reactionTypeId: Long
    val reactionTypeName: String
    val count: Long
}

interface ReactionCountWithActive {
    val reactionTypeId: Long
    val reactionTypeName: String
    val count: Long
    val active: Boolean
}
