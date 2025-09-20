package com.flownews.api.reaction.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ReactionRepository : JpaRepository<Reaction, Long> {
    
    @Query("""
        SELECT rt.name as reactionTypeName, COUNT(r) as count 
        FROM Reaction r 
        JOIN r.reactionType rt 
        WHERE r.event.id = :eventId AND r.isDeleted IS NULL 
        GROUP BY rt.id, rt.name
    """)
    fun findReactionCountsByEventId(eventId: Long): List<ReactionCount>
    
    fun findByUserIdAndEventIdAndIsDeletedIsNull(userId: Long, eventId: Long): Reaction?
}

interface ReactionCount {
    val reactionTypeName: String
    val count: Long
}