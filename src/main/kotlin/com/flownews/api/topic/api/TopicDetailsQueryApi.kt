package com.flownews.api.topic.api

import com.flownews.api.topic.app.TopicDetailsResponse
import com.flownews.api.topic.domain.TopicRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class TopicDetailsQueryApi(private val topicRepository: TopicRepository) {
    @GetMapping("/topics/{id}")
    fun getTopicDetails(@PathVariable id: Long): ResponseEntity<TopicDetailsResponse> {
        val topic = topicRepository.findById(id)
            .orElse(null) ?: return ResponseEntity.notFound().build()

        val sortedEvents = topic.events.sortedBy { it.eventTime }
        val body = TopicDetailsResponse.fromEntity(topic, sortedEvents)
        return ResponseEntity.ok(body)
    }
}