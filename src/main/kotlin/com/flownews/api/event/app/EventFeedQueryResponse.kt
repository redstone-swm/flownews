package com.flownews.api.event.app

import com.flownews.api.event.domain.LikedEvent
import com.flownews.api.topic.domain.Topic
import java.time.LocalDateTime

data class EventFeedQueryResponse(
    val id: Long,
    val topic: TopicSimpleInfo,
    val title: String,
    val description: String,
    val imageUrl: String,
    val eventTime: LocalDateTime,
    val likeCount: Long,
) {
    companion object {
        fun from(likedEvent: LikedEvent) =
            likedEvent.event.run {
                EventFeedQueryResponse(
                    id = requireId(),
                    title = title,
                    description = description,
                    imageUrl = imageUrl,
                    eventTime = eventTime,
                    topic = TopicSimpleInfo(getFirstTopic()),
                    likeCount = likedEvent.event.getLikeCount(),
                )
            }
    }
}

data class TopicSimpleInfo(val id: Long, val title: String) {
    constructor(topic: Topic) : this(
        id = topic.requireId(),
        title = topic.title,
    )
}
