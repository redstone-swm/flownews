package com.flownews.api.topic.domain

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TopicRepository : CrudRepository<Topic, Long> {
    fun findAll(pageable: Pageable): List<Topic>

    fun findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        titleKeyword: String,
        descriptionKeyword: String,
        pageable: Pageable,
    ): List<Topic>

    @Query(
        """
        SELECT t FROM Topic t 
        JOIN t.topicEvents te 
        JOIN UserEventInteraction uei ON te.event.id = uei.event.id 
        WHERE uei.createdAt >= :sinceTime 
        GROUP BY t.id 
        ORDER BY COUNT(uei.id) DESC 
        LIMIT :limit
        """,
    )
    fun findTopKTopicsByInteractionsSince(
        @Param("sinceTime") sinceTime: LocalDateTime,
        @Param("limit") limit: Int,
    ): List<Topic>
}
