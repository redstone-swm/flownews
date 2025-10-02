package com.flownews.api.topic.api

import com.flownews.api.topic.app.TopicListQueryService
import com.flownews.api.topic.app.TopicSummaryResponse
import com.flownews.api.user.infra.CustomOAuth2User
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TopicListQueryApi(
    private val topicListQueryService: TopicListQueryService,
) {
    @GetMapping("/topics")
    fun getAllTopics(
        @AuthenticationPrincipal user: CustomOAuth2User?,
    ): ResponseEntity<List<TopicSummaryResponse>> = ResponseEntity.ok(topicListQueryService.getTopics(user))
}
