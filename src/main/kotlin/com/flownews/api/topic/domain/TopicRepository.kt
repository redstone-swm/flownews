package com.flownews.api.topic.domain

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TopicRepository : CrudRepository<Topic, Long> {
    fun findAll(pageable: Pageable): List<Topic>

    @Query(
        """
            SELECT t.*
            FROM topics t
            JOIN topic_event_map te ON t.id = te.topic_id
            JOIN events e ON te.event_id = e.id
            LEFT JOIN user_event_interactions uei ON te.event_id = uei.event_id 
                AND uei.created_at >= :sinceTime
            WHERE (:category IS NULL OR e.category = :category)
            GROUP BY t.id
            ORDER BY COUNT(uei.id) DESC
            LIMIT :limit
        """,
        nativeQuery = true,
    )
    fun findTopKTopicsByInteractionsSince(
        @Param("sinceTime") sinceTime: LocalDate,
        @Param("category") category: String?,
        @Param("limit") limit: Int,
    ): List<Topic>
}
