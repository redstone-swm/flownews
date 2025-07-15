package com.flownews.api.topic.domain

import BaseEntity
import jakarta.persistence.*

@Entity(name = "topic_histories")
class TopicHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "topic_id")
    val topicId: Long,

    @Column(name = "event_id")
    var eventId: Long,

    @Column(name= "user_id")
    val userId: Long
) : BaseEntity() {

    fun updateLastReadEvent(eventId: Long) {
        this.eventId = eventId.coerceAtLeast(this.eventId)
    }
}
