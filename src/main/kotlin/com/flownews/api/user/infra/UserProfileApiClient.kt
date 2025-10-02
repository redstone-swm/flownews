package com.flownews.api.user.infra

import com.fasterxml.jackson.annotation.JsonProperty
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
    fun updateUserProfileVector(request: UserProfileVectorUpdateRequest) =
        try {
            val headers =
                HttpHeaders().apply {
                    contentType = MediaType.APPLICATION_JSON
                }

            val response =
                restTemplate
                    .exchange(
                        "$recommendationApiUrl/v1/users/{userId}/profile",
                        HttpMethod.POST,
                        HttpEntity<UserProfileVectorUpdateRequest>(request, headers),
                        UpdateUserProfileResponse::class.java,
                    ).body
        } catch (e: Exception) {
            println("Error calling recommendation API: ${e.message}")
        }
}

data class UserProfileVectorUpdateRequest(
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("topic_ids")
    val topicIds: List<Long>,
)

data class UpdateUserProfileResponse(
    @JsonProperty("status")
    val status: String,
    @JsonProperty("user_id")
    val userId: Int,
    @JsonProperty("user_profile_vector")
    val updatedProfileVector: List<Float>,
    @JsonProperty("processed_topics")
    val processedTopics: Int,
)
