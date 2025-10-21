package com.flownews.api.topic.api

import com.flownews.api.topic.app.TopicSearchService
import com.flownews.api.topic.app.TopicSummaryResponse
import com.flownews.api.user.infra.CustomOAuth2User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Topics", description = "토픽 조회 API")
class TopicSearchApi(
    private val topicSearchService: TopicSearchService,
) {
    @Operation(
        summary = "토픽 검색",
        description = "검색어를 포함하는 토픽 제목 또는 설명을 기반으로 토픽을 검색합니다.",
    )
    @GetMapping("/topics/search")
    fun searchTopics(
        @RequestParam query: String,
        @RequestParam(defaultValue = "20") limit: Int,
        @RequestParam(defaultValue = "0") page: Int,
        @AuthenticationPrincipal principal: CustomOAuth2User?,
    ): ResponseEntity<List<TopicSummaryResponse>> {
        if (query.isBlank()) {
            return ResponseEntity.badRequest().build()
        }

        val topics = topicSearchService.search(principal, query, limit, page)
        return ResponseEntity.ok(topics)
    }
}
