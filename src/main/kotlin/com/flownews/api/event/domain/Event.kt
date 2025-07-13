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
    @Column(name = "id")
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    var topic: Topic,

    @Column(name = "title")
    var title: String,

    @Lob
    @Column(name = "description")
    var description: String? = null,

    @Column(name = "image_url")
    var imageUrl: String? = null,

    @Column(name = "event_time")
    var eventTime: LocalDateTime,

    @Column(name = "related_link")
    var relatedLink: String? = null

) : BaseEntity()