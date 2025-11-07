package com.flownews.api.interaction.infra

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
    fun updateProfileByTopic(request: TopicBasedProfileUpdateRequest) {
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
    }

    fun updateProfileByEvent(request: EventBasedProfileUpdateRequest) {
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
    }
}
