package com.flownews.api.topic.api

import com.flownews.api.common.api.ApiResponse
import com.flownews.api.common.app.NoDataException
import com.flownews.api.topic.app.TopicSubscribeRequest
import com.flownews.api.topic.app.TopicSubscribeService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TopicSubscribeApi(private val topicSubscribeService: TopicSubscribeService) {

    @PostMapping("/topics/{topicId}/subscribe")
    fun subscribeTopic(@PathVariable topicId: Long, @RequestBody req: TopicSubscribeRequest): ApiResponse<out Any?> {
        return try {
            topicSubscribeService.subscribeTopic(topicId, req)
            ApiResponse.ok()
        } catch (e: NoDataException) {
            ApiResponse.badRequest(e.message)
        }
    }
}