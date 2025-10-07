package com.flownews.api.topic.domain

import com.flownews.api.event.domain.Event
import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "topic_event_map")
class TopicEvent(
    @EmbeddedId
    val id: TopicEventId,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    @MapsId("topicId")
    val topic: Topic,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @MapsId("eventId")
    val event: Event,
    @Column(name = "created_at")
    val createdAt: LocalDateTime,
)
