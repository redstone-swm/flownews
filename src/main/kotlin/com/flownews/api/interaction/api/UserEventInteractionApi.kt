package com.flownews.api.interaction.api

import com.flownews.api.common.api.CurrentUser
import com.flownews.api.interaction.app.InteractionRecordRequest
import com.flownews.api.interaction.app.InteractionRecordResponse
import com.flownews.api.interaction.app.UserEventInteractionService
import com.flownews.api.user.domain.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/interactions")
@Tag(name = "User Event Interactions", description = "사용자의 이벤트 상호작용 관리")
@SecurityRequirement(name = "bearerAuth")
class UserEventInteractionApi(
    private val userEventInteractionService: UserEventInteractionService,
) {
    @PostMapping("/record")
    @Operation(summary = "사용자 이벤트 상호작용 기록", description = "유저가 피드에서 이벤트와 상호작용한 내역을 기록합니다")
    fun recordInteraction(
        @RequestBody request: InteractionRecordRequest,
        @CurrentUser user: User,
    ): ResponseEntity<InteractionRecordResponse> {
        val interaction =
            userEventInteractionService.recordInteraction(
                userId = user.requireId(),
                eventId = request.eventId,
                interactionType = request.interactionType,
                additionalData = request.additionalData,
            )

        return ResponseEntity.ok(InteractionRecordResponse.ok(interaction))
    }
}
