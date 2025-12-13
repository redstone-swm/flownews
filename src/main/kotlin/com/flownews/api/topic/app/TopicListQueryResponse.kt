package com.flownews.api.topic.app

import com.flownews.api.topic.domain.Topic

data class TopicListQueryResponse(
    val id: Long,
    val title: String,
    val description: String,
) {
    companion object {
        fun from(topic: Topic) =
            TopicListQueryResponse(
                id = topic.requireId(),
                title = topic.title,
                description = topic.description,
            )
    }
}
