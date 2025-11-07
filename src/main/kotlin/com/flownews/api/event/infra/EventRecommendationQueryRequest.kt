package com.flownews.api.event.infra

import com.fasterxml.jackson.annotation.JsonProperty

data class EventRecommendationQueryRequest(
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("category")
    val category: String?,
    @JsonProperty("k")
    val k: Long,
    @JsonProperty("exclude_event_ids")
    val excludeEventIds: List<Long>,
)
