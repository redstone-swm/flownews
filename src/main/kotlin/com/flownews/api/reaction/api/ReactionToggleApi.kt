package com.flownews.api.reaction.api

import com.flownews.api.common.api.CurrentUser
import com.flownews.api.common.app.NoDataException
import com.flownews.api.reaction.app.ReactionToggleResponse
import com.flownews.api.reaction.app.ReactionToggleService
import com.flownews.api.user.domain.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Event Reactions", description = "이벤트 반응 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/events/{eventId}")
class ReactionToggleApi(
    private val reactionToggleService: ReactionToggleService,
) {
    @Operation(
        summary = "이벤트 반응 토글",
        description = "특정 이벤트에 대한 반응을 토글합니다. 같은 반응 타입을 누르면 토글(추가/해제)되고, 다른 반응 타입을 누르면 기존 반응을 해제하고 새로운 반응을 추가합니다.",
    )
    @PostMapping("/reactions/{reactionTypeId}")
    fun toggleReaction(
        @Parameter(description = "이벤트 ID", example = "1")
        @PathVariable eventId: Long,
        @Parameter(description = "반응 타입 ID", example = "1")
        @PathVariable reactionTypeId: Long,
        @CurrentUser user: User,
    ): ResponseEntity<ReactionToggleResponse> =
        try {
            val response = reactionToggleService.toggleReaction(eventId, reactionTypeId, user)
            ResponseEntity.ok(response)
        } catch (e: NoDataException) {
            ResponseEntity.notFound().build()
        }
}
