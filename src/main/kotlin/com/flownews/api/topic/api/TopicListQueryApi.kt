package com.flownews.api.topic.api

import com.flownews.api.topic.app.TopicListQueryService
import com.flownews.api.topic.app.TopicSummaryResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TopicListQueryApi(
    private val topicListQueryService: TopicListQueryService,
) {
//    @GetMapping("/topics", params = ["for=main"])
//    fun getTopics(): ResponseEntity<TopicSectionListQueryResponse> =
//        null

    @GetMapping("/topics")
    fun getAllTopics(): ResponseEntity<List<TopicSummaryResponse>> =
        ResponseEntity.ok(topicListQueryService.getTopics())
}
