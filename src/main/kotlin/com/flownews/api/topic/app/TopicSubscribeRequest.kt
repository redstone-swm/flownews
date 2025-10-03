package com.flownews.api.topic.app

import com.flownews.api.user.domain.User

data class TopicSubscribeRequest(
    val user: User,
    val topicId: Long,
)

data class TopicMultipleSubscribeRequest(
    val user: User,
    val topicId: List<Long>,
)
