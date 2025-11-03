package com.flownews.api.topic.api

import com.flownews.api.common.api.CurrentUser
import com.flownews.api.topic.app.TopicListQueryService
import com.flownews.api.topic.app.TopicSummaryResponse
import com.flownews.api.topic.app.TopicTopKQueryResponse
import com.flownews.api.user.domain.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TopicListQueryApi(
    private val topicListQueryService: TopicListQueryService,
) {
    @GetMapping("/topics")
    fun getAllTopics(
        @CurrentUser user: User?,
        @RequestParam(required = false) limit: Int = 10,
    ): ResponseEntity<List<TopicSummaryResponse>> = ResponseEntity.ok(topicListQueryService.getTopics(user, limit))

    @GetMapping("/topics/topk")
    fun getTopKTopics(
        @RequestParam(required = false) limit: Int = 5,
    ): ResponseEntity<List<TopicTopKQueryResponse>> = ResponseEntity.ok(topicListQueryService.getTopKTopics(limit))
}
