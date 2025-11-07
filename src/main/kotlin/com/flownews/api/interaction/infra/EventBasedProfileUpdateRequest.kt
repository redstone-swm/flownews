package com.flownews.api.interaction.infra

import com.fasterxml.jackson.annotation.JsonProperty
import com.flownews.api.interaction.domain.InteractionType

data class EventBasedProfileUpdateRequest(
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("event_ids")
    val eventIds: List<Long>,
    @JsonProperty("action")
    val action: InteractionType,
) {
    constructor(
        userId: Long,
        eventId: Long,
        interactionType: InteractionType,
    ) : this(userId, listOf(eventId), interactionType)
}
