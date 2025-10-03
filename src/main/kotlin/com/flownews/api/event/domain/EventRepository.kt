package com.flownews.api.event.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : CrudRepository<Event, Long> {
    fun findTopByTopicEventsTopicIdOrderByEventTimeDesc(topicId: Long): Event?

    fun findByTopicEventsTopicIdOrderByEventTimeAsc(topicId: Long): List<Event>
}
