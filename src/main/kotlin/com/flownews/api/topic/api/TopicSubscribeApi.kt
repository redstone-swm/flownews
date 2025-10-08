package com.flownews.api.topic.api

import com.flownews.api.common.app.NoDataException
import com.flownews.api.topic.app.TopicSubscribeRequest
import com.flownews.api.topic.app.TopicSubscribeService
import com.flownews.api.topic.app.TopicSubscriptionToggleResponse
import com.flownews.api.user.infra.CustomOAuth2User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Topic Subscriptions", description = "토픽 구독 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
class TopicSubscribeApi(
    private val topicSubscribeService: TopicSubscribeService,
) {
    @Operation(
        summary = "토픽 구독 토글",
        description = "토픽 구독 상태를 토글합니다. 구독 중이면 해제하고, 구독하지 않았으면 구독합니다.",
    )
    @PostMapping("/topics/{topicId}/toggle-subscription")
    fun toggleSubscription(
        @Parameter(description = "토픽 ID", example = "1")
        @PathVariable topicId: Long,
        @AuthenticationPrincipal principal: CustomOAuth2User,
    ): ResponseEntity<TopicSubscriptionToggleResponse> =
        try {
            val isSubscribe =
                topicSubscribeService.toggleSubscription(
                    TopicSubscribeRequest(
                        user = principal.getUser(),
                        topicId = topicId,
                    ),
                )
            ResponseEntity.ok(TopicSubscriptionToggleResponse(isSubscribed = isSubscribe))
        } catch (e: NoDataException) {
            ResponseEntity.notFound().build()
        }
}
