package com.flownews.api.event.infra

import com.fasterxml.jackson.annotation.JsonProperty

data class EventRecommendationQueryResponse(
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("events")
    val events: List<EventRecommendationQueryItemResponse>,
)

data class EventRecommendationQueryItemResponse(
    @JsonProperty("item_id")
    val eventId: Long,
    @JsonProperty("score")
    val score: Double,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("is_serendipity")
    val isSerendipity: Boolean,
)
