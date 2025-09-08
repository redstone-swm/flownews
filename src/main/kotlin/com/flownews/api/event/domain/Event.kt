package com.flownews.api.event.domain

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
import jakarta.persistence.OneToMany
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
    var description: String,
    @Column(name = "image_url")
    var imageUrl: String,
    @Column(name = "event_time")
    var eventTime: LocalDateTime,
    @Column(name = "related_links")
    var relatedLinks: String,
    @Column(name = "view_count")
    var viewCount: Long = 0,
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    var articles: MutableList<com.flownews.api.article.domain.Article> = mutableListOf()
) : BaseEntity() {
}
