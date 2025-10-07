package com.flownews.api.topic.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class TopicEventId(
    @Column(name = "topic_id")
    val topicId: Long,
    @Column(name = "event_id")
    val eventId: Long,
) : Serializable
