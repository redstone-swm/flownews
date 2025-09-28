package com.flownews.api.recommendation.app

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime

@Service
class RecommendationApiClient(
    @Value("\${recommendation.api.url}")
    private val recommendationApiUrl: String,
    private val restTemplate: RestTemplate = RestTemplate()
) {

    fun getRecommendedEvents(request: RecommendationRequest): RecommendationResponse? {
        return try {
            val headers = HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
            }

            val entity = HttpEntity(request, headers)

            restTemplate.exchange(
                "$recommendationApiUrl/recommend",
                HttpMethod.POST,
                entity,
                RecommendationResponse::class.java
            ).body
        } catch (e: Exception) {
            println("Error calling recommendation API: ${e.message}")
            null
        }
    }
}

data class UserEventInteractionIn(
    @JsonProperty("event_id")
    val eventId: Long,
    @JsonProperty("interaction_type")
    val interactionType: String,
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    val createdAt: LocalDateTime? = null
)

data class RecommendationRequest(
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("interactions")
    val interactions: List<UserEventInteractionIn>,
    @JsonProperty("num_recommended_events")
    val numRecommendedEvents: Int
)

data class RecommendationResponse(
    @JsonProperty("recommended_event_ids")
    val recommendedEventIds: List<Long>
)