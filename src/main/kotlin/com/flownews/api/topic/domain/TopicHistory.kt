package com.flownews.api.topic.domain

import BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "topic_histories")
class TopicHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "topic_id")
    val topicId: Long,
    @Column(name = "event_id")
    var eventId: Long?,
    @Column(name = "user_id")
    val userId: Long,
) : BaseEntity() {
    fun updateLastReadEvent(newEventId: Long?) {
        if (newEventId == null) return
        eventId = eventId?.let { maxOf(it, newEventId) } ?: newEventId
    }
}
