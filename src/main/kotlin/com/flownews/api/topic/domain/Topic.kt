package com.flownews.api.topic.domain

import BaseEntity
import com.flownews.api.topic.domain.event.Event
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.Table

@Entity
@Table(name = "topics")
class Topic(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "title")
    var title: String,
    @Lob
    @Column(name = "description")
    var description: String? = null,
    @Column(name = "image_url")
    var imageUrl: String? = null,
    @OneToMany(mappedBy = "topic", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("eventTime ASC")
    var events: MutableList<Event> = mutableListOf(),
) : BaseEntity() {
    fun requireId(): Long = id ?: throw IllegalStateException("Topic ID cannot be null")
}
