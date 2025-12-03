package com.flownews.api.event.api

import com.flownews.api.common.api.ApiResponse
import com.flownews.api.common.api.CurrentUser
import com.flownews.api.common.app.NoDataException
import com.flownews.api.event.app.EventLikeService
import com.flownews.api.user.domain.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Event Likes", description = "이벤트 좋아요 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
class EventLikeApi(
    private val eventLikeService: EventLikeService,
) {
    @Operation(
        summary = "이벤트 좋아요 토글",
        description = "특정 이벤트에 대한 좋아요를 토글합니다. 이미 좋아요가 되어있으면 해제하고, 그렇지 않으면 좋아요를 추가합니다.",
    )
    @PostMapping("/api/events/{eventId}/like")
    fun toggleLike(
        @Parameter(description = "이벤트 ID", example = "1")
        @PathVariable eventId: Long,
        @CurrentUser user: User,
    ): ApiResponse<Void?> =
        try {
            eventLikeService.toggleLike(eventId, user)
            ApiResponse.ok()
        } catch (e: NoDataException) {
            ApiResponse.nodata()
        }
}
