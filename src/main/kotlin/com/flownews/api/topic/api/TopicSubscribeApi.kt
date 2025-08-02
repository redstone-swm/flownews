package com.flownews.api.topic.api

import com.flownews.api.common.api.ApiResponse
import com.flownews.api.common.app.NoDataException
import com.flownews.api.topic.app.TopicSubscribeRequest
import com.flownews.api.topic.app.TopicSubscribeService
import com.flownews.api.user.app.CustomOAuth2User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TopicSubscribeApi(
    private val topicSubscribeService: TopicSubscribeService,
) {
    @PostMapping("/topics/{topicId}/subscribe")
    fun subscribeTopic(
        @PathVariable topicId: Long,
        @RequestBody req: UserDeviceTokenUpdateRequest,
        @AuthenticationPrincipal principal: CustomOAuth2User,
    ): ApiResponse<out Any?> =
        try {
            topicSubscribeService.subscribeTopic(
                TopicSubscribeRequest(
                    user = principal.getUser(),
                    deviceToken = req.deviceToken,
                    topicId = topicId,
                ),
            )
            ApiResponse.ok()
        } catch (e: NoDataException) {
            ApiResponse.badRequest(e.message)
        } catch (e: IllegalStateException) {
            ApiResponse.badRequest(e.message)
        }
}

data class UserDeviceTokenUpdateRequest(
    val userId: Long,
    val deviceToken: String,
)
