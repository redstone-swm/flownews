package com.flownews.api.event.infra

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class RecommendationApiClient(
    @Value("\${recommendation.api.url}")
    private val recommendationApiUrl: String,
    private val restTemplate: RestTemplate = RestTemplate(),
) {
    fun getRecommendedEvents(
        userId: Long,
        category: String? = null,
        k: Long = 5,
        excludeEventIds: List<Long> = emptyList(),
    ): List<Long> =
        try {
            val headers =
                HttpHeaders().apply {
                    contentType = MediaType.APPLICATION_JSON
                }

            val requestBody =
                EventRecommendationRequest(
                    userId = userId,
                    category = category,
                    k = k,
                    excludeEventIds = excludeEventIds,
                )

            val response =
                restTemplate
                    .exchange(
                        "$recommendationApiUrl/v1/recommendations",
                        HttpMethod.POST,
                        HttpEntity(requestBody, headers),
                        EventRecommendationQueryResponse::class.java,
                    ).body
            response?.events?.map { it.eventId } ?: emptyList()
        } catch (e: Exception) {
            println("Error calling recommendation API: ${e.message}")
            emptyList()
        }
}

data class EventRecommendationQueryResponse(
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("events")
    val events: List<EventRecommendationQueryItemResponse>,
)

data class EventRecommendationRequest(
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("category")
    val category: String?,
    @JsonProperty("k")
    val k: Long,
    @JsonProperty("exclude_event_ids")
    val excludeEventIds: List<Long>,
)

data class EventRecommendationQueryItemResponse(
    @JsonProperty("item_id")
    val eventId: Long,
    @JsonProperty("score")
    val score: Double,
)
