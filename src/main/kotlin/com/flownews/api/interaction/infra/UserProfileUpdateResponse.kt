package com.flownews.api.interaction.infra

import com.fasterxml.jackson.annotation.JsonProperty

data class UserProfileUpdateResponse(
    @JsonProperty("status")
    val status: String,
    @JsonProperty("user_id")
    val userId: Int,
    @JsonProperty("processed_count")
    val processedCount: Int,
)
