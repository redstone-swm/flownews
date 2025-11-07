package com.flownews.api.event.infra

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "recommendation-service",
    url = "\${recommendation.api.url}",
)
interface EventRecommendationApiClient {
    @PostMapping("/v1/recommendations")
    fun getRecommendedEvents(
        @RequestBody request: EventRecommendationQueryRequest,
    ): EventRecommendationQueryResponse
}
