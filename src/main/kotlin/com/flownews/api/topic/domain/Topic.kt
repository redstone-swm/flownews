package com.flownews.api.topic.domain

import BaseEntity
import com.flownews.api.event.domain.Event
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "topics")
class Topic(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @Lob
    @Column(nullable = true)
    var description: String? = null,

    @Column(nullable = true)
    var imageUrl: String? = null,

    @OneToMany(mappedBy = "topic", cascade = [CascadeType.ALL], orphanRemoval = true)
    var events: MutableList<Event> = mutableListOf()
) : BaseEntity()