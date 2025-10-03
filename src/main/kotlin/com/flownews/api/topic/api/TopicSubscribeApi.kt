package com.flownews.api.topic.api

import com.flownews.api.common.app.NoDataException
import com.flownews.api.topic.app.TopicSubscribeRequest
import com.flownews.api.topic.app.TopicSubscribeService
import com.flownews.api.user.infra.CustomOAuth2User
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TopicSubscribeApi(
    private val topicSubscribeService: TopicSubscribeService,
) {
    @PostMapping("/topics/{topicId}/subscribe")
    fun subscribeTopic(
        @PathVariable topicId: Long,
        @AuthenticationPrincipal principal: CustomOAuth2User,
    ): ResponseEntity<out Any?> =
        try {
            topicSubscribeService.subscribeTopic(
                TopicSubscribeRequest(
                    user = principal.getUser(),
                    topicId = topicId,
                ),
            )
            ResponseEntity.ok().build()
        } catch (e: NoDataException) {
            ResponseEntity.badRequest().body(e.message)
        } catch (e: IllegalStateException) {
            ResponseEntity.badRequest().body(e.message)
        }

    @PostMapping("/topics/{topicId}/unsubscribe")
    fun unsubscribeTopic(
        @PathVariable topicId: Long,
        @AuthenticationPrincipal principal: CustomOAuth2User,
    ): ResponseEntity<out Any?> =
        try {
            topicSubscribeService.unsubscribeTopic(
                TopicSubscribeRequest(
                    user = principal.getUser(),
                    topicId = topicId,
                ),
            )
            ResponseEntity.ok().build()
        } catch (e: NoDataException) {
            ResponseEntity.badRequest().body(e.message)
        } catch (e: IllegalStateException) {
            ResponseEntity.badRequest().body(e.message)
        }
}
