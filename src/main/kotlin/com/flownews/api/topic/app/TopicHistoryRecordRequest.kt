package com.flownews.api.topic.app

data class TopicHistoryRecordRequest(
    val topicId: Long,
    val eventId: Long,
    val ipAddress: String,
    val elapsedTime: Int,
    val direction: String
) {
    fun withTopic(topicId: Long): TopicHistoryRecordRequest = this.copy(topicId = topicId)
}
