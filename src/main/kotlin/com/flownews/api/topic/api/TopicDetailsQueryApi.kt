package com.flownews.api.topic.api

import com.flownews.api.common.app.NoDataException
import com.flownews.api.topic.app.TopicDetailsResponse
import com.flownews.api.topic.app.TopicQueryService
import com.flownews.api.topic.app.TopicSummaryResponse
import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.user.app.CustomOAuth2User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class TopicDetailsQueryApi(private val topicQueryService: TopicQueryService) {
    @GetMapping("/topics/{id}")
    fun getTopicDetails(@PathVariable id: Long, @AuthenticationPrincipal user: CustomOAuth2User?): ResponseEntity<TopicDetailsResponse?> {
        return try {
            ResponseEntity.ok(topicQueryService.getTopic(id, user))
        } catch (e: NoDataException){
            ResponseEntity.notFound().build()
        }
    }
}