package com.flownews.api.push.api

import com.flownews.api.common.api.ApiResponse
import com.flownews.api.common.app.NoDataException
import com.flownews.api.push.app.PushMessageSender
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PushMessageSendApi(private val pushMessageSender: PushMessageSender) {

    @PostMapping("/notifications/push", params = ["by=topic"])
    fun sendPushMessageByTopic(@RequestBody req: PushMessageSendRequest): ApiResponse<out Any?> {
        return try {
            ApiResponse.ok(pushMessageSender.sendPushMessages(req.topicId))
        } catch (e: NoDataException) {
            ApiResponse.badRequest(e.message)
        }
    }
}

data class PushMessageSendRequest(val topicId: Long)