package com.flownews.api.topic.domain

import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.UUID

@Embeddable
data class TopicSubscriptionId(
    val visitorId: UUID,
    val topic: Long
) : Serializable
