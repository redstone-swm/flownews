package com.flownews.api.event.api

import com.flownews.api.common.api.CurrentUser
import com.flownews.api.common.app.NoDataException
import com.flownews.api.event.app.EventQueryService
import com.flownews.api.event.app.EventSummaryResponse
import com.flownews.api.user.domain.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Event Query", description = "이벤트 조회 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/events")
class EventQueryApi(
    private val eventQueryService: EventQueryService,
) {
    @Operation(
        summary = "이벤트 상세 조회",
        description = "이벤트 ID로 특정 이벤트의 상세 정보를 조회합니다. 사용자가 인증된 경우 토픽 팔로우 상태가 포함됩니다.",
    )
    @GetMapping("/{id}")
    fun getEvent(
        @PathVariable id: Long,
        @CurrentUser user: User?,
    ): ResponseEntity<EventSummaryResponse> =
        try {
            val eventDetails = eventQueryService.getEvent(id, user)
            ResponseEntity.ok(eventDetails)
        } catch (e: NoDataException) {
            ResponseEntity.notFound().build()
        }
}
