package com.flownews.api.topic.domain

import org.springframework.data.repository.CrudRepository

interface TopicHistoryRepository: CrudRepository<TopicHistory, Long> {
    fun findByTopicIdAndUserId(eventId: Long, userId: Long): TopicHistory?
}