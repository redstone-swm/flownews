package com.flownews.api.topic.domain.event

import BaseEntity
import com.flownews.api.topic.domain.Topic
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
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
    @Column(name = "related_links")
    var relatedLinks: String? = null,
) : BaseEntity() {
    fun getRelatedLinks(): List<String> = relatedLinks?.split(",")?.map { it.trim() } ?: emptyList()
}
