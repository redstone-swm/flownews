package com.flownews.api.topic.app

import com.flownews.api.user.domain.User

data class TopicSubscribeRequest(
    val user: User,
    val topicId: Long,
    val deviceToken: String? = null,
)
