package com.flownews.api.topic.domain

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TopicRepository : CrudRepository<Topic, Long> {
    @Query("SELECT t FROM Topic t ORDER BY function('RAND')")
    fun findAllByRandom(pageable: Pageable): List<Topic>
}