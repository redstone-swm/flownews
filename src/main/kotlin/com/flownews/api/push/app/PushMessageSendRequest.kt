package com.flownews.api.push.app

data class PushMessageSendRequest(
    val topicId: Long?,
) {
    fun requireTopicId(): Long = topicId ?: throw IllegalArgumentException("Topic Id is required")
}
