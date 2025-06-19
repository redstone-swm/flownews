package com.flownews.api.event.domain

import BaseEntity
import com.flownews.api.topic.domain.Topic
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "events")
class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    var topic: Topic,

    @Column(nullable = false)
    var title: String,

    @Lob
    @Column(nullable = true)
    var description: String? = null,

    @Column(nullable = true)
    var imageUrl: String? = null,

    @Column(nullable = false)
    var eventTime: LocalDateTime
) : BaseEntity()