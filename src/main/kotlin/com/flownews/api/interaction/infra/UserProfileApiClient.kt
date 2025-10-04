package com.flownews.api.interaction.infra

import com.fasterxml.jackson.annotation.JsonProperty
import com.flownews.api.interaction.domain.InteractionType
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class UserProfileApiClient(
    @Value("\${recommendation.api.url}")
    private val recommendationApiUrl: String,
    private val restTemplate: RestTemplate = RestTemplate(),
) {
    fun updateProfileByTopic(request: TopicBasedProfileUpdateRequest) =
        try {
            val headers =
                HttpHeaders().apply {
                    contentType = MediaType.APPLICATION_JSON
                }

            restTemplate
                .exchange(
                    "$recommendationApiUrl/v1/users/${request.userId}/profile/topics",
                    HttpMethod.POST,
                    HttpEntity<TopicBasedProfileUpdateRequest>(request, headers),
                    UserProfileUpdateResponse::class.java,
                ).body
        } catch (e: Exception) {
            println("Error calling recommendation API: ${e.message}")
        }

    fun updateProfileByEvent(request: EventBasedProfileUpdateRequest) =
        try {
            val headers =
                HttpHeaders().apply {
                    contentType = MediaType.APPLICATION_JSON
                }

            restTemplate
                .exchange(
                    "$recommendationApiUrl/v1/users/${request.userId}/profile/events",
                    HttpMethod.POST,
                    HttpEntity<EventBasedProfileUpdateRequest>(request, headers),
                    UserProfileUpdateResponse::class.java,
                ).body
        } catch (e: Exception) {
            println("Error calling recommendation API: ${e.message}")
        }
}

data class TopicBasedProfileUpdateRequest(
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("topic_ids")
    val topicIds: List<Long>,
    @JsonProperty("interaction_type")
    val interactionType: InteractionType,
)

data class EventBasedProfileUpdateRequest(
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("event_ids")
    val eventIds: List<Long>,
    @JsonProperty("interaction_type")
    val interactionType: InteractionType,
) {
    constructor(
        userId: Long,
        eventId: Long,
        interactionType: InteractionType,
    ) : this(userId, listOf(eventId), interactionType)
}

data class UserProfileUpdateResponse(
    @JsonProperty("status")
    val status: String,
    @JsonProperty("user_id")
    val userId: Int,
    @JsonProperty("processed_count")
    val processedCount: Int,
)
