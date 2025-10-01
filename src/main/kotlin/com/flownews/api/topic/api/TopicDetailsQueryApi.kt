package com.flownews.api.topic.api

import com.flownews.api.topic.app.TopicDetailsResponse
import com.flownews.api.topic.app.TopicQueryService
import com.flownews.api.user.infra.CustomOAuth2User
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class TopicDetailsQueryApi(
    private val topicQueryService: TopicQueryService,
) {
    @GetMapping("/topics/{id}")
    fun getTopicDetails(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: CustomOAuth2User?,
    ): ResponseEntity<TopicDetailsResponse?> =
//        try {
//            ResponseEntity.ok(topicQueryService.getTopic(id, user))
//        } catch (e: NoDataException) {
//            ResponseEntity.notFound().build()
//        }
        ResponseEntity.notFound().build()
}
