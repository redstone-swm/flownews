package com.flownews.api.event.domain

import BaseEntity
import com.flownews.api.article.domain.Article
import com.flownews.api.bookmark.domain.Bookmark
import com.flownews.api.reaction.domain.Reaction
import com.flownews.api.topic.domain.TopicEvent
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime

@Entity
@Table(name = "events")
class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,
    @Column(name = "title")
    var title: String,
    @Column(name = "description", columnDefinition = "text")
    var description: String,
    @Column(name = "image_url")
    var imageUrl: String,
    @Column(name = "event_time")
    var eventTime: LocalDateTime,
    @Column(name = "view_count")
    var viewCount: Long = 0,
    // @Array(length = 1536)  // 차원 수
    @Column(name = "embedding", columnDefinition = "public.vector(1536)")
    @JdbcTypeCode(SqlTypes.VECTOR)
    var embedding: FloatArray? = null,
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    var articles: MutableList<Article> = mutableListOf(),
    @OneToMany(mappedBy = "event")
    var topicEvents: MutableList<TopicEvent> = mutableListOf(),
    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], orphanRemoval = true)
    var reactions: MutableList<Reaction> = mutableListOf(),
    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], orphanRemoval = true)
    var bookmarks: MutableList<Bookmark> = mutableListOf(),
) : BaseEntity() {
    fun requireId(): Long = id ?: throw IllegalStateException("Event ID cannot be null")
}
