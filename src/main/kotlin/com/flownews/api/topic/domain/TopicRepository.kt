package com.flownews.api.topic.domain

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TopicRepository : CrudRepository<Topic, Long> {
    @Query("SELECT t FROM Topic t WHERE t.id <> :excludeId ORDER BY function('random')")
    fun findAllExceptOneRandom(
        @Param("excludeId") excludeId: Long,
        pageable: Pageable,
    ): List<Topic>
}
