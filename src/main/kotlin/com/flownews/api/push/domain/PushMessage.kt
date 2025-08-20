package com.flownews.api.push.domain

import com.flownews.api.topic.domain.Topic
import com.flownews.api.user.domain.User

data class PushMessage(
    val topicId: Long,
    val userId: Long,
    val deviceToken: String,
    val title: String,
    val content: String,
    val imageUrl: String? = null,
) {
    constructor(topic: Topic, subscriber: User) : this(
        deviceToken = subscriber.deviceToken ?: throw IllegalStateException("User device token cannot be null"),
        title = "새로운 후속기사가 도착했어요",
        content = "${topic.title}의 후속기사를 보려면 클릭",
        imageUrl = topic.imageUrl,
        topicId = topic.requireId(),
        userId = subscriber.requireId(),
    )
}
