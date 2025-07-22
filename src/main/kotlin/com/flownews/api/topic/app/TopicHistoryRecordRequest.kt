package com.flownews.api.topic.app

data class TopicHistoryRecordRequest(
    val topicId: Long,
    val eventId: Long?,
    val ipAddress: String,
    val elapsedTime: Int,
    val direction: String
) {
    fun with(topicId: Long, ipAddress: String): TopicHistoryRecordRequest = this.copy(topicId = topicId, ipAddress = ipAddress)
}
