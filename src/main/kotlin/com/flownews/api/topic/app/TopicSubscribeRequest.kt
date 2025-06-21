package com.flownews.api.topic.app

import java.util.UUID

data class TopicSubscribeRequest(
    val visitorId: UUID?,
    val userAgent: String,
    val ipAddress: String,
    val token: String?
)