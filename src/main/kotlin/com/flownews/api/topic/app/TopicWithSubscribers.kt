package com.flownews.api.topic.app

import com.flownews.api.topic.domain.Topic
import com.flownews.api.user.domain.Visitor

data class TopicWithSubscribers(
    val topic: Topic,
    val subscribers: List<Visitor>
)