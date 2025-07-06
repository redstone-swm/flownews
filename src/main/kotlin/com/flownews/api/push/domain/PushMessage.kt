package com.flownews.api.push.domain

data class PushMessage(
    val topicId: Long,
    val userId: Long,
    val deviceToken: String,
    val title: String,
    val content: String,
    val imageUrl: String? = null,
)
