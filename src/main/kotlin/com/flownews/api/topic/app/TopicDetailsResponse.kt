package com.flownews.api.topic.app

import com.flownews.api.event.app.EventDto
import com.flownews.api.event.domain.Event
import com.flownews.api.topic.domain.Topic

data class TopicDetailsResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val events: List<EventDto>
) {
    companion object {
        fun fromEntity(topic: Topic, events: List<Event>) = TopicDetailsResponse(
            id = topic.id!!,
            title = topic.title,
            description = topic.description,
            imageUrl = topic.imageUrl,
            events = events.map(EventDto::fromEntity)
        )
    }
}