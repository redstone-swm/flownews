package com.flownews.api.event.app

import com.flownews.api.event.domain.Event
import java.time.LocalDateTime

data class EventDto(
    var id: Long,
    var title: String,
    var description: String?,
    var imageUrl: String?,
    var eventTime: LocalDateTime,
    var relatedLink: String?
) {
    companion object {
        fun fromEntity(e: Event) = EventDto(
            id = e.id!!,
            title = e.title,
            description = e.description,
            imageUrl = e.imageUrl,
            eventTime = e.eventTime,
            relatedLink = e.relatedLink
        )
    }
}