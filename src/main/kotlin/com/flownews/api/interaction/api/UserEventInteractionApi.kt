package com.flownews.api.interaction.api

import com.flownews.api.interaction.app.UserEventInteractionService
import com.flownews.api.interaction.domain.InteractionType
import com.flownews.api.interaction.domain.UserEventInteraction
import com.flownews.api.user.infra.CustomOAuth2User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/interactions")
@Tag(name = "User Event Interactions", description = "사용자의 이벤트 상호작용 관리")
@SecurityRequirement(name = "bearerAuth")
class UserEventInteractionApi(
    private val userEventInteractionService: UserEventInteractionService
) {
    
    @PostMapping("/record")
    @Operation(summary = "사용자 이벤트 상호작용 기록", description = "유저가 피드에서 이벤트와 상호작용한 내역을 기록합니다")
    fun recordInteraction(
        @RequestBody request: InteractionRecordRequest,
        @AuthenticationPrincipal user: CustomOAuth2User
    ): ResponseEntity<InteractionRecordResponse> {
        val interaction = userEventInteractionService.recordInteraction(
            userId = user.getUser().requireId(),
            eventId = request.eventId,
            interactionType = request.interactionType,
            additionalData = request.additionalData
        )
        
        return ResponseEntity.ok(
            InteractionRecordResponse(
                id = interaction.id!!,
                success = true,
                message = "상호작용이 성공적으로 기록되었습니다"
            )
        )
    }
    
    @GetMapping("/user/{userId}/event/{eventId}")
    @Operation(summary = "특정 이벤트에 대한 사용자 상호작용 조회")
    fun getUserInteractionsForEvent(
        @PathVariable userId: Long,
        @PathVariable eventId: Long
    ): ResponseEntity<List<UserEventInteraction>> {
        val interactions = userEventInteractionService.getUserInteractionsForEvent(userId, eventId)
        return ResponseEntity.ok(interactions)
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자의 특정 타입 상호작용 조회")
    fun getUserInteractionsByType(
        @PathVariable userId: Long,
        @RequestParam interactionType: InteractionType
    ): ResponseEntity<List<UserEventInteraction>> {
        val interactions = userEventInteractionService.getUserInteractionsByType(userId, interactionType)
        return ResponseEntity.ok(interactions)
    }
    
    @GetMapping("/event/{eventId}/count")
    @Operation(summary = "특정 이벤트의 상호작용 통계 조회")
    fun getInteractionCountForEvent(
        @PathVariable eventId: Long,
        @RequestParam interactionType: InteractionType
    ): ResponseEntity<InteractionCountResponse> {
        val count = userEventInteractionService.getInteractionCountForEvent(eventId, interactionType)
        return ResponseEntity.ok(InteractionCountResponse(count))
    }
}

data class InteractionRecordRequest(
    val eventId: Long,
    val interactionType: InteractionType,
    val additionalData: String? = null
)

data class InteractionRecordResponse(
    val id: Long,
    val success: Boolean,
    val message: String
)

data class InteractionCountResponse(
    val count: Long
)