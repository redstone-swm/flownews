package com.flownews.api.topic.app

import com.flownews.api.topic.domain.Topic
import com.flownews.api.user.domain.User

data class TopicWithSubscribers(
    val topic: Topic,
    val subscribers: List<User>
)