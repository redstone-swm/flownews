package com.flownews.api.topic.api

import com.flownews.api.topic.app.TopicSummaryResponse
import com.flownews.api.topic.domain.TopicRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TopicListQueryApi(private val topicRepository: TopicRepository) {
    @GetMapping("/topics")
    fun getAllTopics(): ResponseEntity<List<TopicSummaryResponse>> {
        return ResponseEntity.ok(topicRepository.findAll().map(TopicSummaryResponse::fromEntity))
    }
}