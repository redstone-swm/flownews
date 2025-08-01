package com.flownews.api.topic.api

import com.flownews.api.topic.app.TopicHistoryRecordRequest
import com.flownews.api.topic.app.TopicHistoryRecordService
import com.flownews.api.user.app.CustomOAuth2User
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TopicHistoryRecordApi(private val topicHistoryRecordService: TopicHistoryRecordService) {

    @PostMapping("topics/{topicId}/read")
    fun recordTopicHistory(
        @PathVariable topicId: Long,
        @AuthenticationPrincipal user: CustomOAuth2User?,
        @RequestBody req: TopicHistoryRecordRequest,
        request: HttpServletRequest
    ): ResponseEntity<Void> {

        topicHistoryRecordService.recordHistory(req.with(topicId, request.remoteAddr), user)

        return ResponseEntity.ok().build()
    }
}