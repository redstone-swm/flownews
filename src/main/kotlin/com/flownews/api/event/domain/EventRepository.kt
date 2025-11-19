package com.flownews.api.event.domain

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : CrudRepository<Event, Long> {
    fun findByIsNoiseFalse(pageable: Pageable): List<Event>

    fun findByIsNoiseFalse(): List<Event>
}
