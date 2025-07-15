package com.flownews.api.topic.app

data class TopicHistoryRecordRequest(
    val topicId: Long,
    val eventId: Long,
    val ipAddress: String,
    val elapsedTime: Int,
    val direction: String
) {
    fun withTopicAndEvent(topicId: Long, eventId: Long): TopicHistoryRecordRequest = this.copy(
        topicId = topicId,
        eventId = eventId
    )
}
