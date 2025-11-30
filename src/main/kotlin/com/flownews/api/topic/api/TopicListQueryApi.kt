package com.flownews.api.topic.api

import com.flownews.api.topic.app.TopicListQueryRequest
import com.flownews.api.topic.app.TopicListQueryResponse
import com.flownews.api.topic.app.TopicListQueryService
import com.flownews.api.topic.app.TopicTopKQueryResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TopicListQueryApi(
    private val topicListQueryService: TopicListQueryService,
) {
    @GetMapping("/api/topics")
    fun getAllTopics(
        @ModelAttribute req: TopicListQueryRequest,
    ): ResponseEntity<List<TopicListQueryResponse>> = ResponseEntity.ok(topicListQueryService.getTopics(req))

    @GetMapping("/api/topics/topk")
    fun getTopKTopics(
        @RequestParam(required = false) limit: Int = 5,
    ): ResponseEntity<List<TopicTopKQueryResponse>> = ResponseEntity.ok(topicListQueryService.getTopKTopics(limit))

    @GetMapping("/api/topics/search")
    fun searchTopics(
        @ModelAttribute req: TopicListQueryRequest,
    ): ResponseEntity<List<TopicListQueryResponse>> {
        return try {
            val topics = topicListQueryService.getTopicsByKeyword(req)
            ResponseEntity.ok(topics)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
}
