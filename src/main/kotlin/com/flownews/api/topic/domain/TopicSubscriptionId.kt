package com.flownews.api.topic.domain

import jakarta.persistence.Embeddable
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.io.Serializable
import java.util.UUID

@Embeddable
data class TopicSubscriptionId(
    @JdbcTypeCode(SqlTypes.CHAR)
    val visitor: UUID,
    val topic: Long
) : Serializable
