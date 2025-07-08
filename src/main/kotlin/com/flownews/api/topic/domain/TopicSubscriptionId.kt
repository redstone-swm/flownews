package com.flownews.api.topic.domain

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class TopicSubscriptionId(
    val user: Long,
    val topic: Long
) : Serializable
