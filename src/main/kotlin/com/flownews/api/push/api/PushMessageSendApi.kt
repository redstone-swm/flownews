package com.flownews.api.push.api

import com.flownews.api.common.app.NoDataException
import com.flownews.api.push.app.PushMessageSendRequest
import com.flownews.api.push.app.PushMessageSender
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PushMessageSendApi(
    private val pushMessageSender: PushMessageSender,
) {
    @PostMapping("/notifications/push", params = ["by=topic"])
    fun sendPushMessageByTopic(
        @RequestBody req: PushMessageSendRequest,
    ): ResponseEntity<out Any?> =
        try {
            ResponseEntity.ok(pushMessageSender.sendPushMessages(req.requireTopicId()))
        } catch (e: NoDataException) {
            ResponseEntity.badRequest().body(e.message)
        }
}
