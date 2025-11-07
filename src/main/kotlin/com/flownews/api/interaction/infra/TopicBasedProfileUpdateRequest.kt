package com.flownews.api.interaction.infra

import com.fasterxml.jackson.annotation.JsonProperty
import com.flownews.api.interaction.domain.InteractionType

data class TopicBasedProfileUpdateRequest(
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("topic_ids")
    val topicIds: List<Long>,
    @JsonProperty("action")
    val action: InteractionType,
) {
    constructor(
        userId: Long,
        topicId: Long,
        interactionType: InteractionType,
    ) : this(userId, listOf(topicId), interactionType)
}
